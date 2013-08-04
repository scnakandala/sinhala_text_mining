package newscrawler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewsPaper {

    String baseUrl = "";
    String titleClass = "";
    String dateClass = "";
    String contentClass = "";
    int startId = 0;
    int endId = 0;
    String fileName = "out.txt";
    Connection connection;
    String newspaperTitle = "";

    public NewsPaper(String url, String title, String date, String content, int start,
            int end, String file, Connection con, String nTitle) {
        baseUrl = url;
        titleClass = title;
        dateClass = date;
        contentClass = content;
        startId = start;
        endId = end;
        fileName = file;
        connection = con;
        newspaperTitle = nTitle;
        setTheNonInitialStartId();
    }

    private void setTheNonInitialStartId() {
        Statement statement;
        try {
            statement = connection.createStatement();
            String query = "select max(articles.id) as max from articles"
                    + " where newspaper ='" + newspaperTitle + "'";
            ResultSet resultset = statement.executeQuery(query);
            resultset.next();
            Integer max = Integer.parseInt(resultset.getString("max"));
            if (max != null) {
                query = "select  link from articles"
                        + " where id =" + max + "";
                resultset = statement.executeQuery(query);
                resultset.next();
                String str = resultset.getString("link");
                String[] arr =  str.split("/|=");
                startId = Integer.parseInt(arr[arr.length-1]);
            }
            startId++;
        } catch (SQLException ex) {
            Logger.getLogger(NewsPaper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeToDatabase(String url, String title, String date, String content) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO articles(newspaper, link, title, date, content) VALUES"
                    + "('" + newspaperTitle + "','" + url + "','" + title + "','"
                    + date + "','" + content + "');";
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(NewsPaper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
