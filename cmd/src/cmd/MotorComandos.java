/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmd;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author andres
 */
public class MotorComandos {
    private File directorioActual;
    private BufferedWriter escritor;
    public MotorComandos(){
        directorioActual=new File(System.getProperty("user.dir"));
        escritor=null;
    }
    
    public String getPrompt(){
        return directorioActual.getAbsolutePath().replace("/", "\\")+">";
    }
    public boolean ModoEscritura(){
        return escritor!=null;
    }
    public ResultadoComando ejecutar(String entrada){
        if(entrada==null){
            return ResultadoComando.normal("");
            
        }
        entrada=entrada.trim();
        if(entrada.equals("")){
            return ResultadoComando.normal("");
        }
        String [] partes;
        String comando, arg;
        partes=entrada.split(" ", 2);
        comando=partes[0];
        arg="";
        if(partes.length>1){
            arg=partes[1];
        }
        if(comando.equals("Mkdir")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Mkdir <nombre>");
            }
            File archivo=new File(directorioActual, arg);
            if(!archivo.exists()){
                archivo.mkdir();
            }
            return ResultadoComando.normal("");
        }
        if(comando.equals("Mfile")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Mfile <nombre.ext>");
            }
            File archivo=new File(directorioActual, arg);
            try{
                archivo.createNewFile();
            }
            catch(Exception e){
            }
            return ResultadoComando.normal("");
        }
        if(comando.equals("Rm")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Rm <nombre>");
            }
            borrar(new File(directorioActual, arg));
            return ResultadoComando.normal("");
        }
        if(comando.equals("Cd")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Cd <carpeta>");
            }
            File archivo=new File(directorioActual, arg);
            if(archivo.exists() && archivo.isDirectory()){
                directorioActual=archivo;
            }
            return ResultadoComando.normal("");
        }
        if(comando.equals("...")){
            File padre=directorioActual.getParentFile();
            if(padre!=null){
                directorioActual=padre;
                
            }
            return ResultadoComando.normal("");
        }
        if(comando.equals("Dir")){
            File[] lista=directorioActual.listFiles();
            String texto="";
            if(lista!=null){
                for(int control=0;control<lista.length;control++){
                    if(lista[control].isDirectory()){
                        texto+="<DIR>";
                    }
                    else{
                        texto+="      ";
                    }
                    texto+=lista[control].getName()+"\n";
                }
            }
            return ResultadoComando.normal(texto);
        }
        if(comando.equals("Date")){
            SimpleDateFormat fecha=new SimpleDateFormat("dd/MM/yyyy");
            return ResultadoComando.normal(fecha.format(new Date()));
        }
        if(comando.equals("Wr")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Wr Archivo.ext");
            }
            File archivo=new File(directorioActual, arg);
            if(!archivo.exists()){
                return ResultadoComando.normal("No existe");
            }
            try{
                escritor=new BufferedWriter(new FileWriter(archivo, true));
            }
            catch(Exception e){
                return ResultadoComando.normal("Error");
            }
            return ResultadoComando.entrarModo("Escriba el texto que desea. EXIT para salir");
        }
        if(comando.equals("Rd")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Rd Archivo.ext");
            }
            File archivo=new File(directorioActual, arg);
            if(!archivo.exists()){
                return ResultadoComando.normal("No existe");
                
            }
            String texto="";
            try{
                BufferedReader lector=new BufferedReader(new FileReader(archivo));
                String linea;
                while((linea=lector.readLine())!=null){
                    texto+=linea+"\n";
                }
                lector.close();
                
            }
            catch(Exception e){
                return ResultadoComando.normal("Error");
            }
            return ResultadoComando.normal(texto);
        }
        return ResultadoComando.normal("Comando inváldio");
    }
    public ResultadoComando escribirLinea(String linea){
        if(escritor==null){
            return ResultadoComando.normal("");
        }
        try{
            if(linea.equals("EXIT")){
                escritor.close();
                escritor=null;
                return ResultadoComando.salirModo("Fin de escritura de archivo");
            }
        }
        catch(Exception e){
            
        }
        return ResultadoComando.normal("");
    }
    private void borrar(File archivo){
        if(!archivo.exists())
            return;
        if(archivo.isDirectory()){
            File[] lista=archivo.listFiles();
            if(lista!=null){
                for(int control=0;control<lista.length;control++){
                    borrar(lista[control]);
                }    
            }
        }
        archivo.delete();
    }
}
