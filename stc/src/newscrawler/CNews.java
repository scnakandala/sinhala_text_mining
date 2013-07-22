package newscrawler;

public class CNews extends NewsPaper{

    public CNews() {
        super("http://lankacnews.com/sinhala/news/",
                "h3", "div.date", "div.description",
                1, 97108, "cnews_out.txt");
    }
}
