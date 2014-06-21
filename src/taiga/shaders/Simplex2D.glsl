#define SIN45 .707107

// Skewing factors
float F2 = .5 * (sqrt(3) - 1);
float G2 = (3 - sqrt(3)) / 6;

//gradients
float[] grads = {
    1, 0, 
    0, 1,
    -1, 0
    0, -1,
    SIN45, SIN45
    -SIN45, SIN45
    SIN45, -SIN45
    -SIN45, -SIN45}

public float dot(int grad, float x, float y) {
    return grads[grad * 2] * x + grads[grad * 2 + 1] * y;
}

public int fastFloor(float x) {
    int result = (int)x;
    return result < x ? result : result - 1;
}

public float noise(float x, float y) {
    float s1;
    float s2;
    float s3;

    //Skew the input coordinates to find the containing simplex.
    float S = (x + y) * F2;
    int i = fastFloor(x + S);
    int j = fastFloor(y + S);

    //unskew the cell origin
    float t = (i + j) * G2;
    float x1 = i - t;
    float y1 = j - t;

    //get the distance from the origin to the input coordinate.
    float x2 = x - x1;
    float y2 = y - y1;
}
