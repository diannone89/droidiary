package droidiary.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DroidiaryDatabaseHelper extends SQLiteOpenHelper{

	
	public DroidiaryDatabaseHelper(Context context) {
	super(context, DB_NAME, null, 1);
	this.myContext= context;
	}
	
	public void onCreate(SQLiteDatabase db) {}
	
	
	public void createDB() throws IOException{
		
		boolean dbExist = checkDataBase();
		 
        if(dbExist){
                //se esiste allora niente..
        }else{
 
                //..altrmenti lo creiamo
                this.getReadableDatabase();
 
                try {
 
                        copyDataBase();
 
                } catch (IOException e) {
 
                        throw new Error("Errore nella creazione della copia del database");
 
                }
        }
        
		/*
		Metodo usato per creare il DB se non esiste
		
		db.execSQL(CREATE_TABLE_ACCOUNT);
		
		db.execSQL(CREATE_TABLE_CONTATTO);
		
		db.execSQL(CREATE_TABLE_APPUNTAMENTO);
		*/
	}

	private void copyDataBase() throws IOException{
		 
        //apre il db esistente
        InputStream myInput = myContext.getAssets().open(DB_NAME);
 
        //
        String outFileName = DB_PATH + DB_NAME;
 
        //apre il db vuoto
        OutputStream myOutput = new FileOutputStream(outFileName);
 
        //
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
        }
 
        myOutput.flush();
        myOutput.close();
        myInput.close();
 
    }
	
	//controlla se il database esiste
	private boolean checkDataBase(){
		 
        SQLiteDatabase checkDB = null;
 
        try{
                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
        }catch(SQLiteException e){}
 
        if(checkDB != null){
 
                checkDB.close();
 
        }
 
        // se non esiste ritorna true
        return checkDB != null ? true : false;
    }
	
	

	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
		
	}
	
	private final Context myContext;
	private static String DB_PATH= "/data/data/droidiary.app/database/";
	private static String DB_NAME = "droidiary";
	/*private static final String CREATE_TABLE_ACCOUNT = "create table account IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, username VARCHAR(6) NOT NULL, password VARCHAR(5) NOT NULL);";
	private static final String CREATE_TABLE_CONTATTO = "create table contatto IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, id_account INT(4) NOT NULL, nome VARCHAR(15) NOT NULL, " +
														"cognome VARCHAR(15) NOT NULL, citta VARCHAR(15), cellulare VARCHAR(11), numerocasa VARCHAR(11)," +
														" mail VARCHAR(11), FOREIGN KEY(id_account) REFERENCES account(_id));";
	private static final String CREATE_TABLE_APPUNTAMENTO = "create table appuntamento IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, id_contatto INT(4), id_account INT(4) NOT NULL," +
			   												"data_ora DATETIME NOT NULL, citta VARCHAR(15), indirizzo VARCHAR(15), descrizione VARCHAR(30), FOREIGN KEY(id_account) REFERENCES account(id_account)," +
			   												"FOREIGN KEY(id_contatto) REFERENCES contatto(_id));";*/
}


