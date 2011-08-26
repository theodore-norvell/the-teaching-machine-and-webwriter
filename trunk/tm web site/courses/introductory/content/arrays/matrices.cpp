void scale(double matrix[], int rows, int cols, double scale){
     for (int r = 0; r < rows; r++)
         for (int c = 0; c < cols; c++)
             matrix[r*cols + c] *= scale;
}

void add(double m1[], double m2[], int rows, int cols){
     for (int r = 0; r < rows; r++)
         for (int c = 0; c < cols; c++)
             m1[r*cols + c] += m2[r*cols + c];
}
