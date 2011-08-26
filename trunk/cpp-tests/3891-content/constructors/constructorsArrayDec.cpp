// Interface declaration
class Array{
public:
	Array(int s);
	~Array();
	int getSize() const;
	double read(int i) const;
	void write(int i, double item);
private:
	double* mpData;
	int mSize;
};
