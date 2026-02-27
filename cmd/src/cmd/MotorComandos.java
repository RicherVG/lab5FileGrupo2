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
        arg = arg.trim();
        if(comando.equals("Mkdir")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Mkdir <nombre>");
            }

            File archivo = new File(directorioActual, arg);

            if(archivo.exists()){
                return ResultadoComando.normal("La carpeta ya existe");
            }

            if(archivo.mkdir()){
                return ResultadoComando.normal("Carpeta creada correctamente");
            } else {
                return ResultadoComando.normal("No se pudo crear la carpeta");
            }
        }
        if(comando.equals("Mfile")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Mfile <nombre.ext>");
            }

            File archivo = new File(directorioActual, arg);

            if(archivo.exists()){
                if(archivo.isFile())
                    return ResultadoComando.normal("El archivo ya existe");
                else
                    return ResultadoComando.normal("Ya existe una carpeta con ese nombre");
            }

            try{
                if(archivo.createNewFile()){
                    return ResultadoComando.normal("Archivo creado correctamente");
                } else {
                    return ResultadoComando.normal("No se pudo crear el archivo");
                }
            } catch(IOException e){
                return ResultadoComando.normal("Error al crear el archivo");
            }
        }
        if(comando.equals("Rm")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Rm <nombre>");
            }

            File objetivo = new File(directorioActual, arg);

            if(!objetivo.exists()){
                return ResultadoComando.normal("No existe: " + arg);
            }

            boolean ok = borrarConResultado(objetivo);

            if(ok){
                return ResultadoComando.normal("Eliminado correctamente: " + arg);
            }else{
                return ResultadoComando.normal("No se pudo eliminar: " + arg);
            }
        }
        if(comando.equals("Cd")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Cd <nombre carpeta>");
            }

            
            if(arg.equals("..")){
                File padre = directorioActual.getParentFile();
                if(padre != null) directorioActual = padre;
                return ResultadoComando.normal("");
            }

            File destino = new File(directorioActual, arg);

            if(!destino.exists()){
                return ResultadoComando.normal("El sistema no puede encontrar la ruta especificada.");
            }

            if(!destino.isDirectory()){
                return ResultadoComando.normal("No es una carpeta: " + arg);
            }

            directorioActual = destino;
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
            File[] lista = directorioActual.listFiles();
            StringBuilder texto = new StringBuilder();

            texto.append(" Directorio de ")
                 .append(directorioActual.getAbsolutePath())
                 .append("\n\n");
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
            if(lista != null){
                for(File archivo : lista){
                    String fecha = formato.format(new Date(archivo.lastModified()));

                    texto.append(fecha).append("  ");

                    if(archivo.isDirectory()){
                        texto.append("<DIR>        ");
                    } else {
                        texto.append(String.format("%10d  ", archivo.length()));
                    }

                    texto.append(archivo.getName()).append("\n");
                }
            }

            return ResultadoComando.normal(texto.toString());
        }
        if(comando.equals("Date")){
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
            return ResultadoComando.normal(fecha.format(new Date()));
        }

        if(comando.equals("Time")){
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
            return ResultadoComando.normal(hora.format(new Date()));
        }
        
        if(comando.equals("Wr")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Wr Archivo.ext");
            }

            File archivo = new File(directorioActual, arg);

            if(!archivo.exists()){
                return ResultadoComando.normal("El archivo no existe");
            }

            if(!archivo.isFile()){
                return ResultadoComando.normal("No es un archivo válido");
            }

            try{
                escritor = new BufferedWriter(new FileWriter(archivo, true)); // true = append
                return ResultadoComando.entrarModo("Escriba el texto. Escriba EXIT para salir.");
            } 
            catch(IOException e){
                return ResultadoComando.normal("Error al abrir el archivo");
            }
        }
        if(comando.equals("Rd")){
            if(arg.equals("")){
                return ResultadoComando.normal("Uso: Rd Archivo.ext");
            }

            File archivo = new File(directorioActual, arg);

            if(!archivo.exists()){
                return ResultadoComando.normal("El archivo no existe");
            }

            if(!archivo.isFile()){
                return ResultadoComando.normal("No es un archivo válido");
            }

            StringBuilder texto = new StringBuilder();

            try(BufferedReader lector = new BufferedReader(new FileReader(archivo))){
                String linea;
                while((linea = lector.readLine()) != null){
                    texto.append(linea).append("\n");
                }
            } catch(IOException e){
                return ResultadoComando.normal("Error al leer el archivo");
            }

            return ResultadoComando.normal(texto.toString());
        }
        return ResultadoComando.normal("Comando invalido");
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
            escritor.write(linea);
            escritor.newLine();
            escritor.flush();
        }
        catch(Exception e){
            return ResultadoComando.normal("Error al escribir");
        }
        return ResultadoComando.normal("");
    }

    private boolean borrarConResultado(File archivo){
        if(!archivo.exists()) return false;

        if(archivo.isDirectory()){
            File[] lista = archivo.listFiles();
            if(lista != null){
                for(File hijo : lista){
                    if(!borrarConResultado(hijo)){
                        return false;
                    }
                }
            }
        }
        return archivo.delete();
    }
}
