package modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo{
    private final int linha;
    private final int coluna;
    private boolean aberto = false;
    private boolean marcado = false;
    private boolean minado = false;
    private List<Campo> vizinhos  = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();
    
    
    Campo(final int linha, final int coluna) {        
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public void registrarObservador(CampoObservador observador) {
    	observadores.add(observador);
    }
    
    private void notificarObservadores(CampoEvento evento) {
    	observadores.stream()
    	.forEach(o -> o.eventoOcorreu(this, evento));
    }
    
    boolean adicionarVizinho( Campo vizinho) {
        boolean linhaDiferente = this.linha != vizinho.linha;
        boolean colunaDiferente = this.coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;
        
        int deltaLinha = Math.abs(this.linha - vizinho.linha);
        int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;
        
        if (deltaGeral == 1 && !diagonal) {
            this.vizinhos.add(vizinho);
            return true;
        }
        if (deltaGeral == 2 && diagonal) {
            this.vizinhos.add(vizinho);
            return true;
        }
        return false;
    }
    
    public void alternarMarcacao() {
        if (!this.aberto) {
            this.marcado = !this.marcado;
            
            if(marcado) {
            	notificarObservadores(CampoEvento.MARCAR);
            }
            else {
            	notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }
    
    public boolean abrir() {
    	
        if (!aberto && !marcado) {
        	if (minado) {
            	notificarObservadores(CampoEvento.EXPLODIR);
            	return true;
            }        
        
        setAberto(true);       
        
        if (this.vizinhancaSegura()) {
            this.vizinhos.forEach(v -> v.abrir());
        }
        return true;
    }else {
    	return false;
    }
        
  }
    
    public boolean vizinhancaSegura() {
        return this.vizinhos.stream().noneMatch(v -> v.minado);
    }
    
    void minar() {
        this.minado = true;
    }
    
    public boolean isMinado() {
        return this.minado;
    }
    
    public boolean isMarcado() {
        return this.marcado;
    }
    
    void setAberto(boolean aberto) {
        this.aberto = aberto;
        
        if(aberto) {
        	notificarObservadores(CampoEvento.ABRIR);
        }
    }
    
    public boolean isAberto() {
        return this.aberto;
    }
    
    public boolean isFechado() {
        return !this.isAberto();
    }
    
    public int getLinha() {
        return this.linha;
    }
    
    public int getColuna() {
        return this.coluna;
    }
    
    boolean objetivoAlcancado() {
        boolean desvendado = !this.minado && this.aberto;
        boolean protegido = this.minado && this.marcado;
        return desvendado || protegido;
    }
    
    public int minasNaVizinhanca() {
        return (int)this.vizinhos.stream().filter(v -> v.minado).count();
    }
    
    void reiniciar() {
        this.aberto = false;
        this.minado = false;
        this.marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }
    
}