package org.AutoIT;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestAutoIT {

	
	String login = "testAutom34life@gmail.com";
	String mdp = "autom34life";
	
	@Test
	public void envoyerMailAvecPj() throws IOException, InterruptedException {
		
		WebDriver driver = new ChromeDriver ();
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		
		//accès à gmail.com
		driver.get("https://gmail.com");
		
		//connexion au compte mail 
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys(login);
		driver.findElement(By.xpath("//span[.='Suivant']")).click();
		driver.findElement(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input")).sendKeys(mdp);
		driver.findElement(By.xpath("//span[.='Suivant']")).click();
		
		//clic nouveau message
		driver.findElement(By.xpath("//div[.='Nouveau message']")).click();
		
		//entrer destinataire
		driver.findElement(By.xpath("//div[@class='wO nr l1']/textarea")).sendKeys(login);
		
		//entrer objet
		driver.findElement(By.xpath("//div[@class='aoD az6']/input")).sendKeys("OBJETTTTT");
		
		//Clic sur le bouton ajouter une pièce jointe
		driver.findElement(By.xpath("//div[@class='a1 aaA aMZ']")).click();
	
		//lance le scripte autoIT
		Runtime.getRuntime().exec("src/main/resources/montest2.exe");
		Thread.sleep(4000);
		
		//Envoie le message
		driver.findElement(By.xpath("//div[.='Envoyer']/div[.='Envoyer']")).click();
		
		
	}


}
