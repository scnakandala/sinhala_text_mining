package newscrawler;

import java.sql.Connection;

public class HiruNews extends NewsPaper{
    public HiruNews(Connection con) {
        super("http://www.hirunews.lk/sinhala/",
            "div.inner-title","div.inner-date", "div.inner-text",
            45000, 64000, "hiruNews_out.txt", con, "hiru_news"
        );
    }
}
