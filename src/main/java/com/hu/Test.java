package com.hu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Test {

    @org.junit.Test
    public void test() throws InterruptedException {
        //配置谷歌浏览器驱动的路径
        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.get("http://www.baidu.com");

        String title = driver.getTitle();
        System.out.printf(title);

        Thread.sleep(2000);
        driver.close();
    }

    /**
     * 根据音乐类型url获得歌单url
     * @throws InterruptedException
     */
    @org.junit.Test
    public void test2() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        List<String> urls = new ArrayList<>();

        driver.get("https://music.163.com/#/discover/playlist/?cat=华语");
        driver.switchTo().frame("g_iframe");

        Thread.sleep(2000);



        //将范围定位到播放列表
        WebElement playList = driver.findElement(By.xpath("//*[@id=\"m-pl-container\"]"));

        System.out.println(playList.getText());

        //拿到所有a标签
        List<WebElement> as=playList.findElements(By.className("msk"));


        int count=0;
        System.out.println(as.size());
        for(WebElement we : as){
            System.out.println(count++);
            //System.out.println(we.getText());
            String a = we.getAttribute("href");
            System.out.println(a);
        }




//        //执行浏览器后退
//        driver.navigate().back();
//        //System.out.printf("back to %s \n", driver.getCurrentUrl());
//
//        //执行浏览器前面
//        driver.navigate().forward();
//        //System.out.printf("forward to %s \n", driver.getCurrentUrl());

        //driver.quit();
    }
}

