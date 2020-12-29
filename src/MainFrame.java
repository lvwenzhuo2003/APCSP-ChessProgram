import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @see javax.swing.JFrame
 * @author lvwenzhuo2003@126.com, 3104969674@qq.com
 * This is the main GUI components in the game.
 */
public class MainFrame extends JFrame implements MouseListener {
    int x;
    int y;
    int flag = 1;//flag controls which chess will go. 1 is black, 2 is white. The default choice is 1.
    boolean canPlay = true;//Controls whether the game can continue.
    int[][] allChess = new int[19][19];//Coordinates of all chesses. 0: no chess; 1: black; 2: white.
    int chessSum = 0;//Total numbers of chesses.
    BufferedImage bgImage = null;//Background image

    JButton withdraw = new JButton("Withdraw");
    JButton restart = new JButton("Restart");
    JButton exit = new JButton("Exit");
    JPanel south = new JPanel();//South panel which contains the buttons listed above.

    public MainFrame(){
        this.setTitle("Chess");
        setSize(630, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            bgImage = ImageIO.read(new File("bgimage.png"));
        } catch(IOException exp){
            exp.printStackTrace();
            JOptionPane.showMessageDialog(null,"Background Image Not Found! ");
            System.exit(0);
        }
        addMouseListener(this);//Attach mouse listener

        south.setLayout(new FlowLayout(FlowLayout.LEFT,60,30));//Set layout

        south.add(restart);
        south.add(withdraw);
        south.add(exit);
        //add buttons: restart, withdraw, exit

        MybuttonListener buttonListener = new MybuttonListener();//Init mouse listener on buttons

        restart.addActionListener(buttonListener);
        withdraw.addActionListener(buttonListener);
        exit.addActionListener(buttonListener);
        //Register all buttons to mouse listener

        this.add(south,BorderLayout.SOUTH);
        setVisible(true);
        //Show all components in SOUTH
    }

    public void paint(Graphics g){
        g.drawImage(bgImage,8, 30, this);
        for (int colum = 58; colum < 600; colum += 30){
            g.drawLine(38, colum, 578, colum);
        }
        for (int rand = 38; rand < 600; rand += 30){
            g.drawLine(rand, 58, rand, 598);
        }
        //Draw chess platform

        g.fillOval(122, 143, 10, 10);
        g.fillOval(484, 143, 10, 10);
        g.fillOval(122, 504, 10, 10);
        g.fillOval(303, 353, 10, 10);
        g.fillOval(484, 503, 10, 10);
        g.fillOval(122, 355, 10, 10);
        g.fillOval(484, 355, 10, 10);
        g.fillOval(303, 145, 10, 10);
        g.fillOval(303, 503, 10, 10);
        //Black dots in platform

        for (int i = 0; i < allChess.length;i++){
            for (int j = 0;j < allChess.length;j++){
                if (allChess[i][j] == 1) {//Perform BLACK move
                    int tempX = i * 30 + 38;//Distance between LEFT border to chess platform
                    int tempY = j * 30 + 58;//Distance between UPPER border to chess platform
                    g.setColor(Color.black);
                    g.fillOval(tempX - 13, tempY - 13, 25, 25);

                } else if (allChess[i][j] == 2){//Perform WHITE move
                    int tempX = i * 30 + 38;
                    int tempY = j * 30 + 58;
                    g.setColor(Color.white);
                    g.fillOval(tempX - 13, tempY - 13, 25, 25);
                }
            }
        }

        if (chessSum > 0){
            g.setColor(Color.red);
            g.drawRect(x * 30 + 38 - 13, y * 30 + 58 - 13, 25, 25);
        }
        chessSum++;
        //System.out.println("Total sum is " + (chessSum - 1));
    }

    public void mouseClicked(MouseEvent event){
        x = event.getX();
        y = event.getY();
        //System.out.println("Mouse x: " + x + " Mouse y: " + y);

        if (canPlay){
            if(x >= 38 && x <= 588 && y >= 58 && y <= 620) {
                x = (x - 38) / 30;//Start with 38, better performance with 19 * 19
                y = (y - 58) / 30;//Start with 58, better performance with 19 * 19
                if (allChess[x][y] == 0){//Do action only if the point is empty
                    //SWITCH: whether BLACK or WHITE will go
                    if (flag == 1){//The turn of BLACK
                        allChess[x][y] = 1;//Put a black chess
                        this.repaint();//Repaint the GUI to show changes
                        this.checkFive();//See whether black wins
                        flag = 2;//Switch to white
                    } else {//The turn of WHITE
                        allChess[x][y] = 2;//Put a white chess
                        this.repaint();//Repaint the GUI to show changes
                        this.checkFive();//See whether white wins
                        flag = 1;//Switch to black
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void checkFive(){
        int color = allChess[x][y];//Save the color of chess which will be actioned.
        int count = 1;//Chess counter init

        for (int i = 1;i < 5;i++){//Check whether the RIGHT side is continuous five chess
            try {
                if (x >= 15) {//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x + i][y]) {
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        count = 1;

        for (int i = 1;i < 5;i++){//Check whether the LEFT side is continuous five chess
            try{
                if (x <= 3){//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x - i][y]){
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        count = 1;

        for (int i = 1;i < 5;i++){//Check whether the LOWer side is continuous five chess
            try{
                if (y >= 15){//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x][y + i]){
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        count = 1;

        for (int i = 1;i < 5;i++){//Check whether the UPper side is continuous five chess
            try {
                if (y <= 3){//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x][y - i]){
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        count = 1;

        for (int i = 1;i < 5;i++){//Check whether the RIGHT UPper side is continuous five chess
            try {
                if (y <= 3 || x >= 15) {//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x + i][y - i]) {
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        count = 1;

        for (int i = 1;i < 5;i++){//Checking whether the LEFT LOWer side is continuous five chess
            try{
                if (x <= 3 || y >= 15){//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x - i][y + i]){
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        count = 1;

        for (int i = 1;i < 5;i++){//Checking whether the LEFT UPper side is continuous five chess
            try{
                if (x <= 3|| y <= 3){//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x - i][y - i]){
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        count = 1;

        for (int i = 1;i < 5;i++) {//Checking whether the RIGHT LOWer side is continuous five chess
            try{
                if (y >= 15 || x >= 15){//Check whether the distance to border is less than 5
                    break;
                }
                if (color == allChess[x + i][y + i]){
                    count++;
                }
                checkWin(count);
            } catch(ArrayIndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
    }

    /**
     * This is the checkWin method uses in other methods
     * @param count The continuous chess numbers passed in.
     */
    public void checkWin(int count) {
        if(count >= 5) {//Five continuous chess
            if(allChess[x][y] == 1) {
                JOptionPane.showMessageDialog(null, "Black Win!");
            }
            if(allChess[x][y] == 2) {
                JOptionPane.showMessageDialog(null, "White Win!");
            }
            canPlay=false;//Game Over
        }
    }

    /**
     * This is the restartGame method uses in button restart.
     */
    public void restartGame(){
        for(int i = 0;i < allChess.length;i++) {
            for(int j = 0;j < allChess.length;j++) {
                allChess[i][j] = 0;
            }
        }
        flag = 1;//Default start with BLACK
        canPlay = true;
        repaint();
    }

    /**
     * This is the withdraw method uses in button withdraw.
     */
    public void withdraw() {
        if (allChess[x][y] != 0) {//Only perform withdraw only if there are chess in the platform
            allChess[x][y] = 0;
            if(flag == 1) {
                flag = 2;
            } else {
                flag = 1;
            }
            repaint();
        }
    }
    class MybuttonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==restart) {//restart button clicked
                restartGame();
            }
            if(e.getSource()==withdraw) {//withdraw button clicked
                withdraw();
            }
            if(e.getSource()==exit) {//exit button clicked
                System.exit(0);
            }
        }
    }
}
