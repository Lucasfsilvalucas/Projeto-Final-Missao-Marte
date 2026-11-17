import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.util.*;
/** Executar a partir da raiz da fase: java tools/GeradorDeAssets.java */
class GeradorDeAssets {
    record I(String nome,int largura,int altura,int quadros,Color cor) {}
    static final I[] IMAGENS={
        new I("fundo",800,600,1,new Color(5,14,31)),new I("terreno",48,48,1,new Color(28,47,69)),
        new I("nave",48,48,1,new Color(63,225,233)),new I("propulsor",48,48,4,new Color(255,151,44)),
        new I("base",48,48,4,new Color(48,207,162)),new I("asteroide",48,48,1,new Color(124,125,145)),
        new I("inimigo",48,48,2,new Color(250,61,84)),new I("professor",48,48,1,new Color(255,201,84)),
        new I("engenheiro",48,48,1,new Color(104,202,249)),new I("astronauta",48,48,1,new Color(190,143,247)),
        new I("desconhecido",48,48,1,new Color(244,61,247)),new I("explosao",48,48,6,new Color(255,150,48)),
        new I("brilho",48,48,5,new Color(247,248,154)),new I("vida_cheia",24,24,1,new Color(248,81,110)),
        new I("vida_vazia",24,24,1,new Color(75,85,98)),new I("vaga_ocupada",24,24,1,new Color(39,214,167)),
        new I("vaga_livre",24,24,1,new Color(63,76,94))
    };
    static final String[] SONS={"mover","selecionar","embarcar","entregar","colisao","vitoria","derrota","musica"};
    public static void main(String[] args)throws Exception{
        Path root=Paths.get("core/assets");Files.createDirectories(root.resolve("imagens"));Files.createDirectories(root.resolve("sons"));
        Random rng=new Random(2026);
        for(I i:IMAGENS){
            BufferedImage b=new BufferedImage(i.largura*i.quadros,i.altura,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g=b.createGraphics();g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            for(int f=0;f<i.quadros;f++){int x=f*i.largura,w=i.largura,h=i.altura;
                if(i.nome.equals("fundo")){
                    g.setPaint(new GradientPaint(0,0,new Color(4,13,32),800,600,new Color(17,40,78)));g.fillRect(0,0,w,h);
                    for(int st=0;st<170;st++){g.setColor(new Color(140+rng.nextInt(116),160+rng.nextInt(96),230,90+rng.nextInt(166)));int xx=rng.nextInt(800),yy=rng.nextInt(600),sz=1+rng.nextInt(3);g.fillOval(xx,yy,sz,sz);}
                    g.setColor(new Color(75,116,180,30));g.fillOval(370,60,370,370);
                }else if(i.nome.equals("terreno")){
                    g.setColor(new Color(27,41,62));g.fillRect(x,0,w,h);g.setColor(new Color(41,61,87));g.drawRect(x+1,1,w-3,h-3);
                    for(int z=0;z<8;z++){g.setColor(new Color(80,105,129,50));g.fillOval(x+rng.nextInt(w),rng.nextInt(h),2,2);}
                }else{
                    int cx=x+w/2,cy=h/2;
                    g.setColor(new Color(0,0,0,0));
                    g.setColor(i.cor);
                    switch(i.nome){
                        case "nave" -> {Polygon ship=new Polygon(new int[]{cx,cx-15,cx,cx+15},new int[]{3,h-10,h-18,h-10},4);g.fillPolygon(ship);g.setColor(Color.WHITE);g.fillOval(cx-5,14,10,10);}
                        case "propulsor"->{int l=9+f*5;g.setColor(new Color(249,215,96,170));g.fillOval(cx-8,19,16,l);g.setColor(new Color(255,116,40,180));g.fillOval(cx-4,20,8,l/2);}
                        case "base"->{g.fillRoundRect(x+5,9,38,30,7,7);g.setColor(Color.WHITE);g.drawRoundRect(x+7,11,34,26,5,5);g.setColor(f%2==0?Color.YELLOW:Color.CYAN);g.fillOval(cx-5,4,10,10);}
                        case "asteroide"->{g.fillPolygon(new int[]{cx-15,cx-7,cx+12,cx+17,cx+9,cx-12},new int[]{20,7,12,29,39,35},6);g.setColor(new Color(205,205,219));g.fillOval(cx-6,17,6,6);}
                        case "inimigo"->{g.fillOval(cx-17,13,34,23);g.setColor(Color.WHITE);g.fillOval(cx-10,18,6,6);g.fillOval(cx+4,18,6,6);g.setColor(f==0?Color.RED:Color.YELLOW);g.fillRect(cx-12,32,24,3);}
                        case "explosao"->{int r=6+f*4;g.setColor(new Color(255,214,79,255-f*30));g.fillOval(cx-r,cy-r,2*r,2*r);g.setColor(new Color(255,83,35,200-f*25));g.fillOval(cx-r/2,cy-r/2,r,r);}
                        case "brilho"->{int r=5+f*4;g.setColor(new Color(255,255,175,255-f*35));g.drawOval(cx-r,cy-r,r*2,r*2);g.drawLine(cx-r-4,cy,cx+r+4,cy);g.drawLine(cx,cy-r-4,cx,cy+r+4);}
                        case "vida_cheia","vida_vazia"->{g.fillOval(cx-8,cy-7,10,10);g.fillOval(cx,cy-7,10,10);g.fillPolygon(new int[]{cx-8,cx+10,cx+1},new int[]{cy,cy,cy+10},3);}
                        case "vaga_ocupada","vaga_livre"->{g.drawRoundRect(x+3,3,w-7,h-7,5,5);if(i.nome.equals("vaga_ocupada"))g.fillOval(cx-5,cy-5,10,10);}
                        default -> {g.fillRoundRect(x+8,4,32,40,10,10);g.setColor(new Color(24,34,55));g.fillOval(cx-10,12,20,15);g.setColor(Color.WHITE);g.fillOval(cx-5,15,10,9);}
                    }
                }
            }
            g.dispose();ImageIO.write(b,"png",root.resolve("imagens/"+i.nome+".png").toFile());
        }
        int rate=22050;
        for(int i=0;i<SONS.length;i++){
            boolean musica=SONS[i].equals("musica");double seconds=musica?8: i==4?.55:i==5?1.1:.24;
            int samples=(int)(rate*seconds);byte[] pcm=new byte[samples*2];
            for(int n=0;n<samples;n++){
                double t=n/(double)rate,fade=Math.min(1,Math.min(t*20,(seconds-t)*12));
                double f=musica? 164.81*(1+(.08*Math.sin(t*1.2))) : (230+i*70)*(1-.3*t/seconds);
                double wave=musica?Math.sin(2*Math.PI*f*t)*.3+Math.sin(2*Math.PI*f*1.5*t)*.15:Math.sin(2*Math.PI*f*t)*.38;
                short value=(short)(wave*fade*15000);pcm[n*2]=(byte)value;pcm[n*2+1]=(byte)(value>>8);
            }
            AudioFormat fmt=new AudioFormat(rate,16,1,true,false);
            try(AudioInputStream stream=new AudioInputStream(new ByteArrayInputStream(pcm),fmt,samples)){
                AudioSystem.write(stream,AudioFileFormat.Type.WAVE,root.resolve("sons/"+SONS[i]+".wav").toFile());
            }
        }
        System.out.println("Gerados: "+IMAGENS.length+" imagens e "+SONS.length+" sons.");
    }
}
