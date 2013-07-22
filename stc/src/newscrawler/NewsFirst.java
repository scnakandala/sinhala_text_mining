package newscrawler;

public class NewsFirst extends NewsPaper {

    public NewsFirst() {
        super("http://www.newsfirst.lk/sinhala/node/",
            "a.active","div.panel-pane.pane-node-created", "div.field.field-name-body.field-type-text-with-summary.field-label-hidden",
            1, 25000, "newsFirst_out.txt"
        );
    }
}
