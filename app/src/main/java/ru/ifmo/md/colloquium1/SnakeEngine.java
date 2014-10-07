package ru.ifmo.md.colloquium1;

import java.util.Vector;

/**
 * Created by Y on 07.10.14.
 */
public class SnakeEngine {
    //Ширена игрового поля в клетках
    public int w = 40;
    //Высотта игрового поля в клетках
    public int h = 60;

    //Карту храним как массив байт
    //0 = пустая клетка
    //1 = клетка заполненная змеёй
    //2 = клетка с едой
    //В перспективе такой способ позволит легко добавить, например, стены
    public byte[][] map;

    public int[] head;
    public Vector<int[]> tail;

    //Направление движения головы храним в виде байта
    // 0 = верх 1 = вправо 2 = вниз 3 = влево
    public byte snakeAngle = 1;

    public SnakeEngine() {
        map = new byte[w][h];
        tail = new Vector<int[]>();
        for(int i = 0; i < 4; i++){
            map[i][0] = 1;
            tail.add(new int[]{i, 0});
            head = new int[]{i, 0};
        }

        for (int i = 0; i < 50; i++)
            createRandomFood();
    }

    //Функция зацикливания координат
    private int validateCoordinate(int d, int s){
        if(d < 0) d = s - 1 ;
        if(d >= s) d = 0;
        return d;
    }

    //Игровой шаг
    public void snakeThink(){
        if(isGameOver) return;

        //Раскладываем позицию головы
        int newx = head[0];
        int newy = head[1];

        //Смещаем её исходя из направления
        if(snakeAngle == 0 | snakeAngle == 2)
            newy += snakeAngle == 0 ? -1 : 1;
        if(snakeAngle == 1 | snakeAngle == 3)
            newx += snakeAngle == 1 ? 1 : -1;

        //Производим зацкливание координат
        newx = validateCoordinate(newx, w);
        newy = validateCoordinate(newy, h);

        byte ahead = map[newx][newy];

        switch(ahead) {
            case 0:
                //Пустая ячейка
                int[] last = tail.get(0);
                tail.remove(0);
                map[last[0]][last[1]] = 0;
                break;
            case 1:
                //Ячейка со змейкой
                gameOver();
                return;
            case 2:
                //Ячейка с едой
                //Просто не удаляем хвост - и змея удлиняется
                //Остаётся лишь увеличить счёт
                score++;
                break;
        }
        head = new int[]{newx,newy};
        tail.add(head);
        map[newx][newy] = 1;

    }

    public int score = 0;
    public boolean isGameOver = false;
    private void gameOver(){
        isGameOver = true;
    }

    public int randompos(int size){
        return (int)Math.floor(Math.random() * size);
    }
    public void createRandomFood(){
        while(true){
            int x = randompos(w);
            int y = randompos(h);
            if(map[x][y] == 0){
                map[x][y] = 2;
                break;
            }
        }
    }

    //Поворот вправо
    public void snakeRight(){
        snakeAngle++;
        if(snakeAngle > 3)
            snakeAngle = 0;
    }

    //Поврот влево
    public void snakeLeft(){
        snakeAngle--;
        if(snakeAngle < 0)
            snakeAngle = 3;
    }
}
