
package integrationtest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import wadp.*;
import wadp.auth.*;
import wadp.service.*;
import wadp.domain.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import wadp.controller.IndexController;
import wadp.repository.CourseRepository;
import wadp.repository.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8080/index"})
public class ChangePasswordTest {
    
    private String password;
    private String name;
    private HtmlUnitDriver driver;
    private WebElement element;
    
    @Autowired 
    UserService service;
   
    @Autowired 
    UserRepository repo;

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private GradeLevelService gradeService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private SkillService skillService;
    
    public ChangePasswordTest() {
    }

    @Before
    public void setUp() {
        name = "test1";
        password = "Testpassword1";
        createUser();
    }
    
    @Test
    public void canChangePasswordAfterLogin() {
        login(name, password);
        getUserdetailsPage();
        passwordChange(password, "Testpassword2");
        assertEquals(repo.findByEmail(name + "gmail.com").getPassword(), "Testpassword2");
        
//
//        when().
//                get("/userdetails").
//        then().
//                body("name", Matchers.is("Mickey Mouse")).
//                body("id", Matchers.is(mickeyId));
    }
    
    
        
    private void createUser() {
        driver = new HtmlUnitDriver();
        driver.get("http://localhost:8080/index");
        element = driver.findElement(By.xpath("//button[contains(.,'Rekisteröidy!')]"));
        element.click();

        element = driver.findElement(By.id("name"));
        element.sendKeys("esimerkki");
        element = driver.findElementByName("email");
        element.sendKeys(name + "@gmail.com");
        element = driver.findElementByName("confirmemail");
        element.sendKeys(name + "@gmail.com");
        element = driver.findElement(By.id("password"));
        element.sendKeys(password);
        element = driver.findElement(By.id("confirmpassword"));
        element.sendKeys(password);
        element = driver.findElement(By.xpath("//button[contains(.,'Rekisteröidy')]"));
        element.submit();
    }
    
    private void login(String name, String password) {
        driver = new HtmlUnitDriver();
        driver.get("http://localhost:8080/index");
        element = driver.findElementByName("email");
        element.sendKeys(name + "@gmail.com");
        element = driver.findElement(By.id("password"));
        element.sendKeys(password);
        element = driver.findElement(By.xpath("//button[contains(.,'Kirjaudu sisään')]"));

        element.click();
    }
    
    private void getUserdetailsPage() {
        driver.getPageSource();
        element = driver.findElement(By.id("dropdown-toggle-menu"));
        element.click();
        element = driver.findElement(By.id("dd-a"));
        element.click();

    }

    private void passwordChange(String password, String confirmPassword) {
        element = driver.findElement(By.id("password"));
        element.sendKeys(password);
        element = driver.findElement(By.id("confirmpasswordpassword"));
        element.sendKeys(confirmPassword);
        element = driver.findElement(By.xpath("//button[contains(.,'Vaihda salasana')]"));
        element.submit();

    }

    private boolean hasMessage(String message) {
        return driver.getPageSource().contains(message);
    }

}
