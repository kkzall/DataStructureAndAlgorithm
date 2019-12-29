public class Board {

    public static final char EMPTY = '.';


    private int N, M;  // 画面行数，列数，根据读取的文件内容确定
    private char[][] data;  // 游戏数据

    private Board preBoard = null;  // 记录当前Board（画面）是从哪个画面得到的
    private String swapString = "";  // 记录哪两个箱子交换从而实现画面的转变

    public Board(String[] lines){
        if (null == lines)
            throw new IllegalArgumentException("lines cannot be null in Board constructor.");

        N = lines.length;
        if(N == 0)
            throw new IllegalArgumentException("lines cannot be empty in Board constructor.");

        M = lines[0].length();
        data = new char[N][M];

        for (int i = 0; i < N; i++){
            if(lines[i].length() != M)
                throw new IllegalArgumentException("All lines' length must be same in Board constructor.");
            for(int j = 0 ; j < M ; j ++)
                data[i][j] = lines[i].charAt(j);
        }
    }

    public Board(Board board, Board preBoard, String swapString){
        if(board == null)
            throw new IllegalArgumentException("board can not be null in Board constructor!");

        this.N = board.N;
        this.M = board.M;
        this.data = new char[N][M];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++)
                this.data[i][j] = board.data[i][j];
        this.preBoard = preBoard;
        this.swapString = swapString;
    }

    public Board(Board board){
        this(board, null, "");
    }

    public int N(){ return N; }
    public int M(){ return M; }
    public char getData(int x, int y){
        if(!inArea(x, y))
            throw new IllegalArgumentException("x, y are out of index in getData!");

        return data[x][y];
    }

    public boolean inArea(int x, int y){
        return x >= 0 && x < N && y >= 0 && y < M;
    }

    public void print(){
        for(int i = 0 ; i < N ; i ++)
            System.out.println(String.valueOf(data[i]));
    }

    public boolean isWin(){

        for(int i = 0 ; i < N ; i ++)
            for(int j = 0 ; j < M ; j ++)
                if(data[i][j] != EMPTY)
                    return false;

        printSwapInfo();

        return true;
    }

    public void swap(int x1, int y1, int x2, int y2){

        if(!inArea(x1, y1) || !inArea(x2, y2))
            throw new IllegalArgumentException("x, y are out of index in swap!");

        char t = data[x1][y1];
        data[x1][y1] = data[x2][y2];
        data[x2][y2] = t;
    }

    // 移动一次箱子之后画面发生的变化
    public void run(){

        do{
            drop();
        }while(match());

        return;
    }

    // 模拟箱子掉落
    private void drop(){
        for (int j = 0; j < M; j++){
            int cur = N - 1;
            for (int i = N - 1; i >= 0; i--)
                if (EMPTY != data[i][j]){
                    swap(cur, j, i, j);
                    cur--;
                }
        }
    }

    private static int d[][] = {{0, 1}, {1, 0}};  // 右，下
    // 模拟消除过程
    private boolean match(){
        boolean isMarched = false;

        boolean tag[][] = new boolean[N][M];
        for (int x = 0; x < N; x++)
            for (int y = 0; y < M; y++)
                if (EMPTY != data[x][y])
                    for (int i = 0; i < 2; i++){
                        int newX1 = x + d[i][0];
                        int newY1 = y + d[i][1];
                        int newX2 = newX1 + d[i][0];
                        int newY2 = newY1 + d[i][1];
                        if (inArea(newX1, newY1) &&
                                inArea(newX2, newY2) &&
                                data[x][y] == data[newX1][newY1] &&
                                data[x][y] == data[newX2][newY2]){
                            tag[x][y] = true;
                            tag[newX1][newY1] = true;
                            tag[newX2][newY2] = true;

                            isMarched = true;
                        }
                    }
        for(int x = 0 ; x < N ; x ++)
            for(int y = 0 ; y < M ; y ++)
                if(tag[x][y])
                    data[x][y] = EMPTY;

        return isMarched;
    }


    public void printSwapInfo(){

        if(preBoard != null)
            preBoard.printSwapInfo();
        System.out.println(swapString);
        return;
    }

}