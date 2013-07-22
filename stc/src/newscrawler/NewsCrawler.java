package newscrawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NewsCrawler {

    public static void main(String[] args) throws IOException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsCrawler crawler = new NewsCrawler();
                NewsPaper paper = new Derana();
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
                NewsPaper paper = new CNews();
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
                NewsPaper paper = new NewsFirst();
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

        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }

    }

    public void crawl(NewsPaper paper) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(paper.fileName));

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
                        titleS = title.get(0).text();
                        titleS = titleS.replaceAll("<.*?>", "");
                        titleS = titleS.replaceAll("</.*?>", "");
                        titleS = titleS.replaceAll("<.*?/>", "");
                    }
                }

                if (paper.dateClass.length() > 0) {
                    Elements date = doc.select(paper.dateClass);
                    if (date.size() > 0) {
                        dateS = date.get(0).text();
                        dateS = dateS.replaceAll("<.*?>", "");
                        dateS = dateS.replaceAll("</.*?>", "");
                        dateS = dateS.replaceAll("<.*?/>", "");
                    }
                }

                if (paper.contentClass.length() > 0) {
                    Elements content = doc.select(paper.contentClass);
                    if (content.size() > 0) {
                        contentS = content.get(0).text();
                        contentS = contentS.replaceAll("<.*?>", "");
                        contentS = contentS.replaceAll("</.*?>", "");
                        contentS = contentS.replaceAll("<.*?/>", "");
                    }
                }

                //we don't need empty content
                if (contentS.isEmpty()) {
                    continue;
                }

                String temp = titleS + "\n" + dateS + "\n" + contentS + "\n\n";
                writer.write(temp);
                writer.flush();
                System.out.println("Valid URL: " + paper.baseUrl + i);

            } catch (IOException e) {
                System.out.println("Invalid URL: " + paper.baseUrl + i);
            }
        }

        writer.close();
    }
}
