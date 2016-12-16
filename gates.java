
import java.io.*;
import java.util.*;
class gates {
    public static boolean[][] graph; //We do this so we can have a filled matrix once we run our BFS (Flood Fill Recursion)
    //NECESSARY Constraints: All the regions are CONNECTED and a gate can only connect TWO distinct regions
    //A distince region can be the region enclosed inside the fences AND the region from the fences to the boundaries of the farm
    public static void main (String [] args) throws IOException {
        //Use a Flood-Fill Approach to fill in the area and to count distinct regions.
        //WARNING: When working with arrays as "graphs, note that it is always array[y][x]. It is NOT array[x][y]
        BufferedReader f = new BufferedReader(new FileReader("gates.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("gates.out")));       
        int N = Integer.parseInt(f.readLine());
        String st = f.readLine();
        boolean[][] graph = new boolean[2*N + 1][2*N + 1];
        int xpos = N;
        int ypos = N;
        int res = 0;
        graph[xpos][ypos] = true;
        for(int i = 0; i < N; i++){ //All we do in here is create a 2D-Array graph
            char dir = st.charAt(i);
            if(dir == 'N'){ //We go TWO spaces in our array, because it allows us to count a distinctive region. Keep this in mind: This is a necessary practice when modeling regions with 2D-Arrays
                ypos--;
                graph[ypos][xpos] = true;
                ypos--;
                graph[ypos][xpos] = true;
            }
            if(dir == 'S'){
                ypos++;
                graph[ypos][xpos] = true;
                ypos++;
                graph[ypos][xpos] = true;
            }
            if(dir == 'E'){
                xpos++;
                graph[ypos][xpos] = true;
                xpos++;
                graph[ypos][xpos] = true;
            }
            if(dir == 'W'){
                xpos--;
                graph[ypos][xpos] = true;
                xpos--;
                graph[ypos][xpos] = true;
            }
        }
        //VERY Weird error thing: The X's and the Y's get mixed up. This is what screwed up my cases. I have no clue why it does this, but be aware of the mix-up in the x and y variables.
        for(int i = 0; i < 2*N; i++){
            for(int j = 0; j < 2*N; j++){
                if(!graph[i][j]) {
                    res++; //Every time we find a new distinctive region, we increment our "res" variable which just counts the number of distinct regions
                    //Addition explanation: The reason is because we use flood-fill. Keep this in mind: flood-fill fills the entire distinct region (of course, if you are only doing NESW direction flood-fill). It does NOT fill the entire graph. So thus, if we fill an entire distinct region and the method does this, each time we re-iterate the method is each time we fill a distinct region, which means we add one to the distinct region count each time we perform the method
                    fill(i, j, graph); //Not recursive, but will still fill up the entire distinct region with one method call
                }
            }
        }     
        out.println(res - 1); //Because the answer is just the number of distinct regions - 1
        out.close();                           
    }
    public static void fill(int x, int y, boolean[][] graph){ //This is the iterative version of flood-fill
        LinkedList<Point> queue = new LinkedList<Point>();  //This is our queue. This means the first element we insert is the first element to be removed. This will be important in our iterative version of flood-fill, as we will store the available points for visiting
        queue.add(new Point(x, y)); //This will be our starting point. We created a Point class to store this coordinate. Because this is not a recursive method, we can seemlessly intialize our first point in the distinct region
        graph[x][y] = true; //Obviously, because we already visited our starting point, we will mark it as true so we don't repeat over it
        int[][] d = {{-1, 1, 0, 0}, {0, 0, -1, 1}}; //d[0] = [-1, 1, 0, 0] => d[1] = [0, 0, -1, 1] //This will store the directions. The same thing as declaring 8 statements to add to the queue, but just a cleaner way to do the same thing with a for-loop.
        while(!queue.isEmpty()){ //We do an iterative flood-fill. In other words, we are just doing a iterative Breadth-First-Search graph traversal
            Point currentPoint = queue.removeFirst(); //We remove the parent node and return it. In other words, the first element we inserted will be removed and returned
            for(int i = 0; i < 4; i++){ //because there are 4 directions to loop through in our direction array, we will loop 4 times
                //In simple terms, this loop just means to look for open spaces by looking in all 4 directions
                //Flood-Fill directions goes respectively West, East, South, North.
                int nextX = currentPoint.x + d[0][i]; //This will be the x-coordinate of our next direction
                int nextY = currentPoint.y + d[1][i]; //This will be the y-coordinate of our next direction
                if(checkInBound(nextX, nextY, graph.length) && !graph[nextX][nextY]){ //First, we check if the direction we are headed to is actually in the farm coordinates (otherwise we get an ArrayOutofIndex). Second, we check if the direction we're headed to has already been visited before/has a fence. If so, we do not want to re-include it
                    graph[nextX][nextY] = true; //If the next possible space is qualified, then we now visit this space!
                    queue.add(new Point(nextX, nextY)); //We add this in the queue. Now we repeat this for all of the points in the queue!
                }
            }
        }
        return;
    }
    public static boolean checkInBound(int x, int y, int N){ //This code checks if it is in the bounds of the graph array 
        if((x >= 0 && y >= 0) && (x < N && y < N)) return true;
        else return false;
    }
    public static class Point{
        public int x, y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}