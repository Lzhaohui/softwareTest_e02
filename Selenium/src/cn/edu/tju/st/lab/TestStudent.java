package cn.edu.tju.st.lab;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestStudent {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private DealExcel excel;

  @Before
  public void setUp() throws Exception {
	  //初始化驱动
	  String driverPath = System.getProperty("user.dir") + "/src/resources/driver/geckodriver.exe";
	  System.setProperty("webdriver.gecko.driver", driverPath);
	  driver = new FirefoxDriver();
	  //设定测试目标的url
	  baseUrl = "http://121.193.130.195:8800/login";
	  //设置超时时间
	  driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
	  //初始化excel数据
	  excel= DealExcel.create("E://aa//软件测试名单.xlsx");
  }

  @Test
  public void testStudent() throws Exception {
	//测试数据的准备
	int numOfStudent = excel.getNum();
	int rightCounts = 0;
	String[] ids = new String[excel.getNum()];
	String[] names = new String[excel.getNum()];
	String[] urls = new String[excel.getNum()];
	//访问网址
	driver.get(baseUrl);
	for(int i = 0; i < numOfStudent; i++){
		//填写用户名
		driver.findElement(By.name("id")).sendKeys(excel.getId(i));
		//填写密码
		driver.findElement(By.name("password")).sendKeys(excel.getPassword(i));
		//登陆
		WebElement btn = driver.findElement(By.id("btn_login"));
		btn.sendKeys(Keys.ENTER);
		//抓取信息
		names[i] = driver.findElement(By.id("student-name")).getText();
		ids[i] = driver.findElement(By.id("student-id")).getText();
		urls[i] = driver.findElement(By.id("student-git")).getText();
		//判断信息的准确性
		if((urls[i].replaceAll(" ", "").equals(excel.getUrl(i))) && (ids[i].replaceAll(" ", "").equals(excel.getId(i))) && (names[i].replaceAll(" ", "").equals(excel.getName(i)))){
			System.out.println("Right: No." + i + ": " + names[i] + ": " + ids[i] + ": " + urls[i]);
			rightCounts++;
		}else{
			System.out.println("Wrong: No." + i + ": " + names[i] + ": " + ids[i] + ": " + urls[i]);
			System.out.println("Correct: No." + i + ": " + excel.getName(i) + ": " + excel.getId(i) + ": " + excel.getUrl(i));
		}
		//登出
		driver.findElement(By.id("btn_logout")).click();
		//返回主界面
		driver.findElement(By.id("btn_return")).click();
	}
	//统计
	System.out.println("Right: " + rightCounts);
	System.out.println("Wrong: " + (numOfStudent - rightCounts));
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
  }
}


