package org.shoper.http;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.shoper.http.apache.HttpClient;
import org.shoper.http.apache.HttpClientBuilder;
import org.shoper.http.exception.HttpClientException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by ShawnShoper on 2016/10/27.
 */
public class Weibo_login_test {
    @Before
    public void initProxyPool() throws FileNotFoundException {
//        ProxyServerPool.importProxyServer(new File("src/main/resources/proxyip.ls"), Charset.forName("utf-8"));
    }

    @Test
    public void login_logout_selenium() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
//        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        JavascriptExecutor driver_js = (JavascriptExecutor) driver;
        driver.get("http://weibo.com");
//        driver.manage()
//        TimeUnit.SECONDS.sleep(10);
        for (; ; ) {
            try {
                driver.findElement(By.id("pl_login_form"));
                break;
            } catch (Exception e) {

            }
            TimeUnit.MILLISECONDS.sleep(200);
        }
        WebElement txtbox = driver.findElement(By.id("loginname"));
        txtbox.sendKeys("xiehao3692@vip.qq.com");
        WebElement passwordBox = driver.findElement(By.name("password"));
        passwordBox.sendKeys("xiehao,930825.");
        try {
            Thread thread = new Thread(() -> {
                driver_js.executeScript("document.getElementsByClassName(\"W_btn_a btn_32px\")[0].click()");
            });
            thread.start();
            WebElement logout = driver.findElement(By.className(""));
            while (!logout.isEnabled())
                TimeUnit.MILLISECONDS.sleep(200);
            System.out.println("login over");
            TimeUnit.SECONDS.sleep(10);

            driver_js.executeScript("document.getElementsByClassName(\"gn_func\")[0].childNodes[0].click()");
//            suda-data="key=account_setup&value=quit"
            System.out.println(driver_js.executeScript("document.body.scrollTop = 5000"));
            System.out.println(driver.findElement(By.tagName("html")).getText());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            StringBuilder stringBuilder = new StringBuilder();
            driver.manage().getCookies().forEach(c -> {
                stringBuilder.append(c.getName() + "=" + c.getValue() + ";");
            });
            System.out.println(stringBuilder.toString().substring(0, stringBuilder.length() - 1));
        }
//        WebElement submit = driver.findElement(By.cssSelector("a[action-type='btn_submit']"));
//        submit.click();
//        btn.click();

        driver.close();
    }

    @Test
    public void test() throws IOException, HttpClientException, InterruptedException, TimeoutException, ExecutionException {
        HttpClient build = HttpClientBuilder.custom().setUrl("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100206&refer_flag=0000015010_&from=feed&loc=nickname&is_all=1&pagebar=0&pl_name=Pl_Official_MyProfileFeed__28&id=1002065357651574&script_uri=/u/5357651574&feed_type=0&page=1&pre_page=1&domain_op=100206&__rnd=1477578444488").build();
        build.setCookies("SUB=_2A251FmA8DeTxGedG6VYS9i3LzT6IHXVWYtb0rDV8PUNbmtBeLRbekW8-gPiEXvGe4f9yruJtGVol8sxlKQ..;_lastvisited=av6YELhA9j0CAW64zLAKWVnV%2C%2Cav6YELhA9j0CAW64zLAKWVnV64JxyZhn%2Ciusgb0xw%2Ciusgb0xw%2C1%2Ce14b1e6c%2Cav6YELhA9j0CAW64zLAKWVnV;YF-Page-G0=46f5b98560a83dd9bfdd28c040a3673e;_umdata=70CF403AFFD707DF1349F2A4974DFA9E972B29CE7433561EC09F0C42B1F2030C785AE9C72F9C68C5231CFFB0EFAB5C872973BE19A35179C3C18BC1A6F648E8B6898B4F5AD2E38401539B4360846BC3C283BB2EB508928E024A6E995400E29695;cad=QVioyOPnbkAcSrfDMxdFHvWEJllukpfFk64anmW4elM=0001;WBStorage=86fb700cbf513258|undefined;login_sid_t=8464b75e409c66f6936da9abf29663f0;Apache=9543591205896.55.1477578860506;YF-V5-G0=c998e7c570da2f8537944063e27af755;SINAGLOBAL=9543591205896.55.1477578860506;l=AiwsetEYoNXWbU80ti0w1TrzfABe5dCP;YF-Ugrow-G0=57484c7c1ded49566c905773d5d00f82;wvr=6;cap=8eaf;SSOLoginState=1477578860;cna=av6YELhA9j0CAW64zLAKWVnV;SCF=AqABHPxSywZd4EJIS8-ODZZo2KuV5r-lpqE5iLnGR8lhcb9bM0sGzatHy-igmXN48S5gGZaBFaVVMJIZjX8i0jc.;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhdPz3lCecbA_hp6RKPxGfu5JpX5K2hUgL.Fo2ReoB0SoeNSoz2dJLoIEqLxK-LBK2L1--LxKML12qLB-B_TCH8SCHFxbHWSEH8Sb-RSF-ReBtt;_s_tentry=-;un=xiehao3692@vip.qq.com;SUHB=0mxFrPKMmYvRLG;ALF=1509114859;isg=Am1tOFAYyd6L3K2yD1ALd-kJfA9cWKGccAmFAK9yqYRzJo3YdxqxbLt1Zk07;ULV=1477578860511:1:1:1:9543591205896.55.1477578860506:");
        System.out.println(build.doGet());
    }
}
