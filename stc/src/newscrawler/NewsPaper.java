package newscrawler;

public class NewsPaper {
    String baseUrl = "";
    String titleClass = "";
    String dateClass = "";
    String contentClass = "";
    int startId = 0;
    int endId = 0;
    
    String fileName = "out.txt";
    
    public NewsPaper(String url,String title,String date,String content,int start,int end,String file){
        baseUrl = url;
        titleClass = title;
        dateClass = date;
        contentClass = content;
        
        startId = start;
        endId = end;
        
        fileName = file;
    }
}
