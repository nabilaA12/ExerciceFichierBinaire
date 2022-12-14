import java.awt.Font;
import java.io.*;
import java.text.DecimalFormat;

import javax.swing.*;

public class Employe {

    static RandomAccessFile sortie;
    static JTextArea affichage;
    static File fichier;
    static  final  int   tnom=20;
    static BufferedReader data;
     // 4 octets pour numéro du film, 20 octets pour nom et prenom 20*2
    // 12 octets pour la catégorie et 4 octets pour la durée. 
    // 8 octet pour les Doubles
    // Le système ajoute au début de chaque string 2 octets '\\u'
    // pour indiquer l'encodage (UTF). Comme nous avons 2 strings
    // alors faudra ajouter à ta taillle de l'enregistrement 4 octets de plus.
    // Voilà donc le dernier 4 dans le calcul de la taille.
    static final  int  t_enreg=4+(tnom*2)+8+2+2;
    private static final DecimalFormat df= new DecimalFormat("0.00");
  
    public static void chargerFichierBinaire() throws IOException{
      int nEmp;
      String nom;
      String prenom;
      Double salaire;

      sortie = new RandomAccessFile(fichier, "rw");

      String ligne;
      String []elems;
      data = new BufferedReader(new FileReader("src/employe.txt"));
      ligne = data.readLine();//Lire la premi�re ligne du fichier
      while (ligne != null) {//Si ligne == null alors ont a atteint la fin du fichier
          elems = ligne.trim().split(";");//elems[0] contient le num�ro du livre;elems[1] le titre et elems[2] les pages
          nEmp=Integer.parseInt(elems[0]);  
          nom=formaterString(elems[1],tnom);
          prenom=formaterString(elems[2],tnom);
          salaire=Double.parseDouble(elems[3]);
          
          sortie.writeInt(nEmp);
          sortie.writeUTF(nom);
          sortie.writeUTF(prenom);
          sortie.writeDouble(salaire);
          
          ligne = data.readLine();
          }
          sortie.close();
    }
    /////////// formatter strings///////////////
    public static String formaterString(String leString, int tailleMax){
      int taille = tailleMax-leString.length();
      for(int i=0;i<taille;i++){
          leString+=" ";
      }
      return leString;
  }
  ////////////////// Afficher entete///////////////////////
  public static void afficherEntete() {
    affichage.setFont(new Font("Courier New", Font.BOLD, 12));
    affichage.append("\n\nLISTE D'EMPLOYÉS'"+"\n------------------------\n");
    affichage.append(String.format("%-20s %-20s %-25s %-25s","NUMÉRO DE l'employé","Nom","Prénom","Salaire")+"\n");
    
}
  
 
  ///////////////////////////recherche qui retourne pos ////////////////////
  public static long getAdresse(int num) 

  {
    
   long pos=((num/100)-1)*t_enreg;

    return pos;

  }

////// affichage///////////
public static void afficherTout() throws IOException 

  {
  
   // System.out.println("je suis la");
   sortie = new RandomAccessFile(fichier, "rw");
   
     int compteur=0,id;
     String nom,prenom;
     double salaire;
   //  int i=0;

    try
    {
      afficherEntete();
      while(true)
        {
          
         // sortie.seek(i);
             id = sortie.readInt();
             nom=sortie.readUTF();
             prenom=sortie.readUTF();
             salaire=sortie.readDouble();
           //  i+=t_enreg;
         if(id!=-1 && id!=0){
           affichage.append(String.format("%-20s %-20s %-25s %-25s",id,nom,prenom,salaire)+"\n");
           System.out.println("Numero: "+ id +"\n" + "Prenom: "+ prenom + "\n" +"Nom: "+nom + "\n" +"Salaire: "+salaire+"\n");
           
           
            compteur++;}
          
      
        }
        
    }
    catch (EOFException ex)
    {
        System.out.println("Total number " + compteur);
        affichage.append("Nombre d employes : " + compteur);
        System.out.println("End of file reached!");
        sortie.close();
    }
  
  }
   //////// recherche//////////////
  public static void rechercherEmploye() throws IOException

  {
    sortie = new RandomAccessFile(fichier, "rw");

    long pos;

    int num,id;

    String nom,prenom;
  
    //int i=0;
    double salaire;
    
    Boolean trouv=false;

    num=Integer.parseInt(JOptionPane.showInputDialog("Entrer le numero de l'employe a afficher:"));
    try{
      while(true)
      {
        
     //     sortie.seek(i);
          id = sortie.readInt();
          if(id==num){
            trouv=true;
            System.out.println("oui trouvee");
          }
          nom=sortie.readUTF();
          
          prenom=sortie.readUTF();
         
          salaire=sortie.readDouble();
         
   
        //  i+=t_enreg;
         

          

      }
    
    }
      catch (EOFException ex)
      {
          
          System.out.println("End of file reached!");
      }
    if (trouv==true){
      pos=getAdresse(num);

      sortie.seek(pos);
  
      num=sortie.readInt();
      nom=sortie.readUTF();
      prenom=sortie.readUTF();
  
      salaire=sortie.readDouble();
     
      afficherEntete();
     
      affichage.append(String.format("%-20s %-20s %-25s %-25s",num,nom,prenom,salaire)+"\n");
      JOptionPane.showMessageDialog(null, affichage, "Liste de tous les employés", JOptionPane.PLAIN_MESSAGE);
    
    }
    else{
      JOptionPane.showMessageDialog(null, "Employé :"+num+" n existe pas !", "Message", JOptionPane.PLAIN_MESSAGE);
    }

  sortie.close();

  }
 
      public static void ajouter() throws IOException

      {
        sortie = new RandomAccessFile(fichier, "rw");
    
        int num,id;
        String nom,prenom;
        Boolean trouv=false;
        Double salaire;
      
        num=Integer.parseInt(JOptionPane.showInputDialog("Entrer le numero de l employe:"));
        try{
        while(true)
        {
     
            id = sortie.readInt();
            if(id==num){
              trouv=true;
              System.out.println("oui trouvee");
            }
            nom=sortie.readUTF();
            
            prenom=sortie.readUTF();
           
            salaire=sortie.readDouble();
    
        }
      
      }
        catch (EOFException ex)
        {
            
            System.out.println("End of file reached!");
        }
        if(trouv==true){
          
          JOptionPane.showMessageDialog(null, "Employé :"+num+" existe déja !", "Message", JOptionPane.PLAIN_MESSAGE);
        }
         
        else{ 
         
          nom=JOptionPane.showInputDialog("Entrer le nom de l'employe:");
          prenom=JOptionPane.showInputDialog("Entrer le prenom de l'employe:");
    
          salaire=Double.parseDouble(JOptionPane.showInputDialog("Entrer le salaire de l'employe:"));
    
          sortie.seek(getAdresse(num));//seq: 100, 200, 300,….
    
         sortie.writeInt(num);
    
         sortie.writeUTF(formaterString(nom,tnom) );
         sortie.writeUTF(formaterString(prenom,tnom));
    
         sortie.writeDouble(salaire);
         JOptionPane.showMessageDialog(null, "Employé :"+num+" ajouté avec succés!", "Message", JOptionPane.PLAIN_MESSAGE);}
        
         sortie.close();
      }

  
  ////////////salaire moyen///////////////////
  public static void salaireMoyen() throws IOException

  {
    sortie = new RandomAccessFile(fichier, "rw");
  
    Double somme=Double.parseDouble("0");
 
    //df.format(salaire);
    int compteur=0;
   // sortie.seek(0);
    try
    {
      while(true){
        sortie.readInt();
        sortie.readUTF();
        sortie.readUTF();
        somme+=sortie.readDouble();
       System.out.println(somme);
        compteur++;
      }
   //   System.out.println(somme/compteur);
          }
          catch (EOFException ex)
    {
        System.out.println("Total number " + compteur);
        System.out.println("End of file reached!");
    }
  
    System.out.println(somme/compteur);
    
  JOptionPane.showMessageDialog(null,"Salaire moyen : "+df.format(somme/compteur) );
  sortie.close();
  }
/////////////////modifier salaire/////////////////
public static void ModifierSalaire() throws IOException

{
  sortie = new RandomAccessFile(fichier, "rw");


  Double nouveauSalaire;

  Long pos;
  int num; int id;

  boolean trouv=false;

  num=Integer.parseInt(JOptionPane.showInputDialog("Entrer le numero de l employe:"));
  try{
  while(true)
  {
      id = sortie.readInt();
      if(id==num){
        trouv=true;
        System.out.println("pas trouvee");
      }
     sortie.readUTF();
      
     sortie.readUTF();
     
      sortie.readDouble();
   

  }

}
  catch (EOFException ex)
  {
      
      System.out.println("End of file reached!");
  }
  if(trouv==true){
    
    pos=getAdresse(num);
    sortie.seek(pos); // on peut faire sortie.seek(pos+4+20+20+4) et on retire sortie.readInt(); et sortie.readUTF(); et sortie.readUTF();
    nouveauSalaire=Double.parseDouble(JOptionPane.showInputDialog("Entrer le nouveau salaire de l'employé: "+num));
    sortie.readInt();
    sortie.readUTF();
    sortie.readUTF();
    sortie.writeDouble(nouveauSalaire);
    JOptionPane.showMessageDialog(null, "Salaire modifié pour employé :"+num , "Message", JOptionPane.PLAIN_MESSAGE);

  }
  else{
    JOptionPane.showMessageDialog(null, "Employé :"+num+" n existe pas!", "Message", JOptionPane.PLAIN_MESSAGE);
  }

 sortie.close();

}
////////// supprimer un employe///////////////
public static void SupprimerEmploye() throws IOException{
  sortie = new RandomAccessFile(fichier, "rw");
  
  Long pos;
  int num;
  int id;
  Boolean trouv=false;
 
  num=Integer.parseInt(JOptionPane.showInputDialog("Entrer le numero de l'employe a supprimer"));
  try{
    while(true)
    {
        id = sortie.readInt();
        if(id==num){
          trouv=true;
          System.out.println("trouvee");
        }
        sortie.readUTF();
        
        sortie.readUTF();
       
        sortie.readDouble();
 
    }
  
  }
    catch (EOFException ex)
    {
        
        System.out.println("End of file reached!");
    }
    if(trouv==true){
  pos=getAdresse(num);
  sortie.seek(pos);
  sortie.writeInt(-1);
  sortie.writeUTF("                    ");
  sortie.writeUTF("                    ");
  sortie.writeDouble(0.00);
  JOptionPane.showMessageDialog(null, "Employé :"+num +" a été supprimé ! ", "Message", JOptionPane.PLAIN_MESSAGE);}
  else{
    JOptionPane.showMessageDialog(null, "Employé :"+num+" n existe pas!", "Message", JOptionPane.PLAIN_MESSAGE);
  }
  sortie.close();
}
   
///////////////// main //////////////////
public static void main(String[] args)throws IOException {
  fichier = new File("src/employe.bin") ;
  if(!fichier.exists()){
    chargerFichierBinaire();
  }



int choix;
int choixAffichage;

do

{

     choix=Integer.parseInt(JOptionPane.showInputDialog("\n 1-Afficher les donnees"

     +"\n 2-calculer le salaire moyens\n 3-Ajouter un employé\n 4-Modifier le salaire d'un employé\n 5-Supprimer un employé\n 6-Quitter\n\nEntrer votre choix:"));

switch (choix){



case 1: 
do{
choixAffichage=Integer.parseInt(JOptionPane.showInputDialog("\n 1-Afficher tous les employés "+"\n 2-Afficher un employé spécifique "+"\n 3-Quitter"+"\n\nEntrer votre choix :"));
switch(choixAffichage){
case 1:affichage=new JTextArea(20,20);
afficherTout();
JOptionPane.showMessageDialog(null, affichage, "Liste de tous les employés", JOptionPane.PLAIN_MESSAGE);
break;
case 2:affichage=new JTextArea(20,20);
rechercherEmploye();

break;
case 3:break;
default: JOptionPane.showMessageDialog(null, "Choix invalide !", "Message", JOptionPane.PLAIN_MESSAGE);
}


}while (choixAffichage!=3);

break;



case 2:salaireMoyen();break;

case 3:ajouter();break;
case 4:ModifierSalaire();break;
case 5:SupprimerEmploye();break;
case 6:sortie.close();
System.exit(0) ;
default: JOptionPane.showMessageDialog(null, "Choix invalide !", "Message", JOptionPane.PLAIN_MESSAGE);

}

}while(choix!=4);

}

}
    
 

