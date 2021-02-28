import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
/*
*Assignment 7. Book Recommender 
* Oct 19th 2020
*/

public class BookRecommender{
    
    public static void main(String []args) throws FileNotFoundException{
        String books[] = bookTitleReader("books.txt");
        int ratings[][] = ratingsReader("ratings.txt");
    
        Scanner in = new Scanner(System.in);
        int userRatings[] = new int[books.length];
        for(int i = 0; i < 20; i++) {
            System.out.print("Enter a rating from 1 to 5 for\n" + books[i] + " or -1 if you haven't read it: ");
            userRatings[i] = in.nextInt();
        }
        double scores[] = getSimilarityScores2(userRatings, ratings);
    
        double results[] = weightedAvg(ratings, scores);
    
        for(int i = 0; i < results.length; i++) {
            if(userRatings[i] > 0 && userRatings[i] < 6) {
            results[i] = 0;
            }
        }
        System.out.println("You should read " + books[getRec(results)] + " next.");
        
    }

    public static String[] bookTitleReader(String filename) throws FileNotFoundException{
        Scanner in = new Scanner(new File(filename));;
        String result[] = new String[20];
        for(int i = 0; i < 20; i++) {
            if(in.hasNextLine()) {
                result[i] = in.nextLine();
            }
        }
        return result;
    }
    public static int[][] ratingsReader(String filename) throws FileNotFoundException{
        Scanner in = new Scanner(new File(filename));
        int result[][] = new int[30][20];
        for(int i = 0; i < 30; i++){
            for(int j = 0; j < 20; j++) {
                if(in.hasNextInt()) {
                    result[i][j] = in.nextInt();
                }
            }
        }
    return result;
    }
    
    public static double[] getSimilarityScores1(int ratings[][]){
        double similarities[] = new double[ratings.length];
        for(int i = 0; i < 30; i++) {
            similarities[i] = computeCosineSimilarity(ratings[i]);
        }
        return similarities;
    }
    public static double[] getSimilarityScores2(int user[], int ratings[][]){
        double p1 = computeCosineSimilarity(user);
        double similarities[] = getSimilarityScores1(ratings);
        double result[] = new double[30];
        for(int i = 0; i < 30; i++) { 
            int both = 0;
            for(int j = 0; j < 20; j++) {
                if(user[j] > 0 && user[j] < 6 && ratings[i][j] < 6) {
                    both += user[j] * ratings[i][j];
                }
            }
            result[i] = (both / (p1 * similarities[i]));
            }   
            return result;
        }
        
    public static double computeCosineSimilarity(int ratings[]) {
        int total = 0;
        for(int i = 0; i < 20; i++){
            if(ratings[i] > 0 && ratings[i] < 6) {
                total += ratings[i] * ratings[i];
            }
        }
        return Math.sqrt(total);
    }
    public static double[] weightedAvg(int ratings[][], double scores[]) {
        double avg[] = new double[20];
        for(int i = 0; i < 20; i++) {
            double bookRatingsTotal = 0;
            int count = 0;
            for(int j = 0; j < 30; j++) {
                if(ratings[j][i] > 0 && ratings[j][i] < 6) {
                    bookRatingsTotal += ratings[j][i] * scores[j];
                    count++;
                }
            }
            avg[i] = bookRatingsTotal / count;
        }
        return avg;
    }
    public static int getRec(double results[]) {
        double max = 0;
        int index = 0;
        for(int i = 0; i < results.length; i++) {
            if(results[i] > max) {
                max = results[i];
                index = i;
            }
        }
        return index;
    }
}
