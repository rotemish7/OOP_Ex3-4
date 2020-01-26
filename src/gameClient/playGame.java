package gameClient;

public class playGame 
{
	public static void main(String[] args)
	{
		
		//Create a SimpleGameClinet to run the game
		SimpleGameClient DGame = new SimpleGameClient();	

		
		//Game thread
		Thread T_Game = new Thread(DGame); 	
		
		
		 //play game
		T_Game.start();		
	}
}
