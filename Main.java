import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
 
public class Main {
	public static DB db = new DB();
	static Scanner k = new Scanner(System.in);
     static String str = k.next();
     static String str1 = k.next();
	public static void main(String[] args) throws SQLException, IOException {
		System.out.println("Enter the website and keyword");
		db.runSql2("TRUNCATE Record;");
		processPage("http://"+str);
	}
 
	public static void processPage(String URL) throws SQLException, IOException{
		//check if the given URL is already in database
		String sql = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = db.runSql(sql);
		if(rs.next()){
 
		}else{
			//store the URL to database to avoid parsing again
			sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.execute();
 
			//get useful information
			Document doc = Jsoup.connect("http://"+str).get();
 
			if(doc.text().contains(str1)){
				System.out.println(URL);
			}
 
			//get all links and recursively call the processPage method
			Elements questions = doc.select("a[href]");
			for(Element link: questions){
				if(link.attr("href").contains(str))
					processPage(link.attr("abs:href"));
			}
		}
	}
}

