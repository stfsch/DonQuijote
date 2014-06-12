package de.rwth_aachen.kbsg.dq;

import java.util.List;

public class Zustand {
	public String belegung[][];
	public Zustand(){
		belegung=new String [3][8];
		for (int i=0; i<3; i++){
			for (int j=0; j<8;j++){
				belegung[i][j]= "empty";
			}
		}
	}
	private boolean isMühle(int rahmen,int position, String farbe){
		if(position%2==1&&position!=7){
			if(belegung[rahmen][position+1]==farbe&&belegung[rahmen][position-1]==farbe){
				return true;
			}
			else if(belegung[0][position]==farbe&&belegung[1][position]==farbe&&belegung[2][position]==farbe){
				return true;
			}
			else{
				return false;
			}
		}
		else if(position==7){
			if(belegung[rahmen][0]==farbe&&belegung[rahmen][6]==farbe){
				return true;
			}
			else if(belegung[0][position]==farbe&&belegung[1][position]==farbe&&belegung[2][position]==farbe){
				return true;
			}
			else{
				return false;
			}
		}
		else if(position%2==0){
			if(isMühle(rahmen,position+1, farbe)==true){
				return true;
			}
			else{
				return false;
			}
		}
		else if(position!=0){
			if(isMühle(rahmen, position-1,farbe)==true){
				return true;
			}
			else{
				return false;
			}
		}
		else if(position==0){
			if(isMühle(rahmen,7,farbe)==true){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
}
	public String [][] getBelegung(){
		return belegung;
	}
	
	public List <Zustand> getErreichbareZustände (String farbe, Zustand currentZustand, String phase){
		List <Zustand> erreichbareZustände= new List<Zustand>();
		switch(phase){
		case "setzen": for(int i=0; i<3; i++){
			for(int j=0; j<8; j++){
				if(belegung[i][j]=="empty"){
					Zustand zustand=new Zustand();
					zustand=currentZustand;
					belegung[i][j]= farbe;
					erreichbareZustände.add(zustand);
				}
				return erreichbareZustände;
			}
		}
		break;
		case "ziehen": for(int i=0;i<3;i++){
			for(int j=0;j<8;j++){
				if(belegung[i][j]==farbe&&j!=7){
					if(belegung[i][j+1]=="empty"){
						Zustand zustand = new Zustand();
						zustand=currentZustand;
						belegung[i][j+1]=farbe;
						belegung[i][j]="empty";
						erreichbareZustände.add(zustand);
						}
				}
				else if (belegung[i][j]==farbe&&j==7){
					if(belegung[i][0]=="empty"){
						Zustand zustand = new Zustand();
						zustand=currentZustand;
						belegung[i][j+1]=farbe;
						belegung[i][j]="empty";
						erreichbareZustände.add(zustand);
					}
				}
				else if(belegung[i][j]==farbe&&j!=0){
					if(belegung[i][j-1]=="empty"){
						Zustand zustand = new Zustand();
						zustand=currentZustand;
						belegung[i][j-1]=farbe;
						belegung[i][j]="empty";
						erreichbareZustände.add(zustand);
					}
				}
				else if(belegung[i][j]==farbe&&j==0){
					if(belegung[i][7]=="empty"){
						Zustand zustand = new Zustand();
						zustand=currentZustand;
						belegung[i][7]=farbe;
						belegung[i][j]="empty";
						erreichbareZustände.add(zustand);
					}
				}
			if(j%2==1){
				if(belegung[i][j]==farbe&&i!=2){
					if(belegung[i+1][j]=="empty"){
						Zustand zustand = new Zustand();
						zustand=currentZustand;
						belegung[i+1][j]=farbe;
						belegung[i][j]="empty";
						erreichbareZustände.add(zustand);
					}
				}
			}
			else if(belegung[i][j]==farbe&&i!=0){
					if(belegung[i-1][j]=="empty"){
						Zustand zustand = new Zustand();
						zustand=currentZustand;
						belegung[i-1][j]=farbe;
						belegung[i][j]="empty";
						erreichbareZustände.add(zustand);
					}
				}
			}
		}
		return erreichbareZustände;
		break;
		case "springen":
		case "springenWhite":
		case "springenBlack":if(phase=="springenWhite"&& farbe=="black"){	
			phase="ziehen";
			getErreichbareZustände(farbe,currentZustand,phase);
			phase="springenWhite";
			}
		else if (phase=="springenBlack"&&farbe=="white"){
			phase="ziehen";
			getErreichbareZustände(farbe,currentZustand,phase);
			phase="springenBlack";
		}
		else{
		for(int i=0; i<3; i++){
			for(int j=0; j<8; j++){
				if(belegung[i][j]==farbe){
					for(int k=0; k<3; k++){
						for(int l=0; l<8; l++){
							if(belegung[k][l]=="empty"){
								Zustand zustand=new Zustand();
								zustand=currentZustand;
								belegung[k][l]= farbe;
								belegung[i][j]="empty";
								erreichbareZustände.add(zustand);
							}
						}
					}
				}
			}
		}
	}
		return erreichbareZustände;
		break;
		case "wegnehmen": if(farbe=="white"){
			farbe="black";
		}
		else{
			farbe="white";
		}
			for(int i=0;i<3; i++){
			for(int j=0;j<8;j++){
				if(belegung[i][j]==farbe){
					if(currentZustand.isMühle(i,j,farbe)==false){
						Zustand zustand=new Zustand();
						zustand=currentZustand;
						belegung [i][j]="empty";
						erreichbareZustände.add(zustand);
					}
				}
			}
		}
	}
}

	
	public int getAnzahlSteine(String farbe){
		int steine=0;
		switch(farbe){
		case "": for(int i=0;i<3;i++){
			for(int j=0;j<8;j++){
				if(belegung[i][j]=="empty"){
					
				}
				else if(belegung[i][j]=="black"^belegung[i][j]=="white"){
					steine++;
				}
			}
		}
		return steine;
		case "white": for(int i=0;i<3;i++){
			for(int j=0;j<8;j++){
				if(belegung[i][j]=="empty"||belegung[i][j]=="black"){
					
			}
				else if(belegung[i][j]=="white"){
					steine++;
				}
			}
		}
		return steine;
		case "schwarz": for(int i=0;i<3;i++){
			for(int j=0;j<8;j++){
				if(belegung[i][j]=="empty"||belegung[i][j]=="white"){
					
			}
				else if(belegung[i][j]=="black"^belegung[i][j]=="white"){
					steine++;
				}
			}
		}
		return steine;
		default: return steine;
			}
		}
	}
