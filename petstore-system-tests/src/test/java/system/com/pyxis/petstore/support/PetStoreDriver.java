package system.com.pyxis.petstore.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.pyxis.petstore.domain.Item;

public class PetStoreDriver {

    private final WebDriver webdriver;
	private static ApplicationContext persistenceContext;
	
	static {
		persistenceContext = new ClassPathXmlApplicationContext("persistenceContext.xml");
	}

    public PetStoreDriver() {
        this.webdriver = new ChromeDriver();
    }

    public <T extends PageObject> T navigateTo(Class<T> pageClass) throws Exception {
        webdriver.navigate().to(Routes.urlFor(pageClass));
        T page = PageFactory.initElements(webdriver, pageClass);
        page.assertOnRightPage();
        return page;
    }

    public void dispose() {
        webdriver.quit();
    }

	public void addToInventory(Item item) {
		HibernateTemplate hibernateTemplate = persistenceContext.getBean(HibernateTemplate.class);
		hibernateTemplate.saveOrUpdate(item);
	}
}
