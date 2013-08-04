package newscrawler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NewsCrawler {

    private static Connection connection;

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        NewsCrawler.connection = DriverManager
                .getConnection("jdbc:mysql://localhost/stm?useUnicode=true&characterEncoding=utf-8",
                "root", "");

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsCrawler crawler = new NewsCrawler();
                NewsPaper paper = new Derana(NewsCrawler.connection);
                try {
                    crawler.crawl(paper);
                } catch (IOException ex) {
                    Logger.getLogger(NewsCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsCrawler crawler = new NewsCrawler();
                NewsPaper paper = new CNews(NewsCrawler.connection);
                try {
                    crawler.crawl(paper);
                } catch (IOException ex) {
                    Logger.getLogger(NewsCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsCrawler crawler = new NewsCrawler();
                NewsPaper paper = new NewsFirst(NewsCrawler.connection);
                try {
                    crawler.crawl(paper);
                } catch (IOException ex) {
                    Logger.getLogger(NewsCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsCrawler crawler = new NewsCrawler();
                NewsPaper paper = new HiruNews(NewsCrawler.connection);
                try {
                    crawler.crawl(paper);
                } catch (IOException ex) {
                    Logger.getLogger(NewsCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(t1);
        executor.execute(t2);
        executor.execute(t3);
        executor.execute(t4);


        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }

    }

    public void crawl(NewsPaper paper) throws IOException {

        for (int i = paper.startId; i <= paper.endId; i++) {
            try {
                String titleS = "", contentS = "", dateS = "";
                Document doc = Jsoup.connect(paper.baseUrl + i)
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4;"
                        + " en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                        .userAgent("Mozilla")
                        .method(Method.GET)
                        .get();

                if (paper.titleClass.length() > 0) {
                    Elements title = doc.select(paper.titleClass);
                    if (title.size() > 0) {
                        titleS = removeTags(title.get(0).text());
                    }
                }

                if (paper.dateClass.length() > 0) {
                    Elements date = doc.select(paper.dateClass);
                    if (date.size() > 0) {
                        dateS = removeTags(date.get(0).text());
                    }
                }

                if (paper.contentClass.length() > 0) {
                    Elements content = doc.select(paper.contentClass);
                    if (content.size() > 0) {
                        contentS = removeTags(content.get(0).text());
                    }
                }

                //we don't need empty content
                if (contentS.isEmpty()) {
                    continue;
                }

                // for databse write
                paper.writeToDatabase(paper.baseUrl + i, titleS, dateS, contentS);

                System.out.println("Valid URL: " + paper.baseUrl + i);

            } catch (Exception e) {
                System.out.println("Invalid URL: " + paper.baseUrl + i);
            }
        }
    }

    public static String removeTags(String text) {
        text = text.replaceAll("<.*?>", "");
        text = text.replaceAll("</.*?>", "");
        text = text.replaceAll("<.*?/>", "");

        return text;
    }
}
