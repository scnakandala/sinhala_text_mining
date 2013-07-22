package newscrawler;

public class Derana extends NewsPaper {

    public Derana() {
        super("http://sinhala.adaderana.lk/news.php?nid=",
            "h2.completeNewsTitle", "p.newsDateStamp", "div.newsContent",
            6000, 35376, "derana_out.txt"
        );
    }
}
