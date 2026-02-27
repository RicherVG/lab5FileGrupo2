/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cmd;

/**
 *
 * @author hermi
 */
public class ResultadoComando {
    private String mensaje;
    private boolean entrar;
    private boolean salir;
    
    public ResultadoComando(String mensaje, boolean entrar, boolean salir){
        this.mensaje=mensaje;
        this.entrar=entrar;
        this.salir=salir;
    }
    
    public static ResultadoComando normal(String msg){
        return new ResultadoComando(msg,false,false);
    }
    
    public static ResultadoComando entrarModo(String msg){
        return new ResultadoComando(msg,true,false);
    }
    
    public static ResultadoComando salirModo(String msg){
        return new ResultadoComando(msg,false,true);
    }
    
    public String getMensaje(){
        return mensaje;
    }
    
    public boolean isEntrar(){
        return entrar;
    }
    
    public boolean isSalir(){
        return salir;
    }
}