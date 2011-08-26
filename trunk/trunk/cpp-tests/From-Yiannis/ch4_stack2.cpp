#include <iostream>

using namespace std ;

const int PLITHOS=5;

struct typos_stoivas
    { 
      int korifi;
      int pinakas[PLITHOS];
    } ;




void dimiourgia(typos_stoivas *stoiva);
int  keni(typos_stoivas stoiva);
void exagogi(typos_stoivas *stoiva,int *stoixeio,int *ypoxeilisi);
void othisi(typos_stoivas *stoiva,int stoixeio,int *yperxeilisi);
void ektypwsh(typos_stoivas *stoiva);


void main() 
{


      struct typos_stoivas r_stoiva;
      int yperx,element,flag;

	  dimiourgia(&r_stoiva);
      
	  
	  while (flag != 0)
	  {
		  //system("cls");
		  cout<< " Options\n\n";
		  cout<< " 0: To exit the program\n";
		  cout<< " 1: Insert an integer into the stack\n";
		  cout<< " 2: Extract from the stack\n";
	      cout<< " 3: Print the stack's contents\n";
		  
		  cout << "\nGive the option:";
		  cin  >> flag;
		  switch (flag) {
	  case 1:cout << "Give an integer:";
	         cin >> element;
			 othisi(&r_stoiva,element,&yperx);
			 if (yperx == 1)
			 {	 
		     cout << "The stack is full. You can not insert new element\n";
	         cout << "\nPress Enter to continue";
			 //getchar();getchar();
			 }break;
	  case 2:exagogi(&r_stoiva,&element,&yperx); 
		     if (yperx == 1) 
				 cout << "The stack is empty\n";
			 else
			 cout << "Extracted element: " << element << "\n";
			 cout << "\nPress Enter to continue";
			 //getchar();getchar();
			 break;
	  case 3:cout << "The stack's contents are the following\n";
		     ektypwsh(&r_stoiva);
	         cout << "\nPress Enter to continue";
			 //getchar();getchar();
			 break;
	  default: cout <<" ";break;
	  }
cout << '\n';		  
      }
   cout << '\n';

    
}


/*������� �����������*/

void dimiourgia(typos_stoivas *stoiva)
{
 /*
 *	���: 		�����
 *	����: 		���������� ����� �������
*/
	stoiva->korifi = -1;
}

int  keni(typos_stoivas stoiva)
{
 /*
 *	���: 		���������� ��� stoiva
 *	����: 		���������� 1 �� � ������ ����� ���� ������ 0
*/
	return (stoiva.korifi == -1);
}

void exagogi(typos_stoivas *stoiva,int *stoixeio,int *ypoxeilisi)
{
 /*
 *	���: 		�� ���� ������
 *	����: 		�������� �� �������� ��� �� ������
*/
	if (keni(*stoiva))
		*ypoxeilisi = 1;
	else
	{
		*ypoxeilisi = 0;
		*stoixeio = stoiva->pinakas[stoiva->korifi];
		stoiva->korifi--;
	}
}

void othisi(typos_stoivas *stoiva,int stoixeio,int *yperxeilisi)
{
 /*
 *	���: 		���������� ��� stoiva
 *	����: 		������ �� �������� ��� ������
*/
	if (stoiva->korifi == PLITHOS -1)
		*yperxeilisi = 1;
	else
	{
		*yperxeilisi = 0;
		stoiva->korifi++;
		stoiva->pinakas[stoiva->korifi]=stoixeio;
	}
}

void ektypwsh(typos_stoivas *stoiva)
{
	/* �������� ��� ������������ ��� ������� */
      int i;

	if ((stoiva->korifi) > -1)
		for (i=0; i<= stoiva->korifi;i++)
		{
           cout << stoiva->pinakas[i];
		   cout << '\n';
		}
    else
		cout << "The stack is empty\n";
}

