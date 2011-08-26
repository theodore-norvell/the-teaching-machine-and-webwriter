LINKFLAGS = -L C:/MinGW/lib/gcc-lib/mingw32/3.2.3/ -lstdc++
LINK = g++ ${LINKFLAGS}
CFLAGS = -ggdb -c
COMPILE = g++ ${CFLAGS}

clean : *.o
	erase *.o *.exe
	
user : user.o
	${LINK} -o user user.o ${LINKFLAGS}

user.o : user.cpp
	${COMPILE} user.cpp

user1 : user1.o
	${LINK} -o user1 user1.o ${LINKFLAGS}

user1.o : user1.cpp
	${COMPILE} user1.cpp

copyConsArray : copyConsArray.o
	${LINK} -o copyConsArray copyConsArray.o ${LINKFLAGS}

copyConsArray.o : copyConsArray.cpp
	${COMPILE} copyConsArray.cpp

pointers4 : pointers4.o
	${LINK} -o pointers4 pointers4.o ${LINKFLAGS}

pointers4.o : pointers4.cpp
	${COMPILE} pointers4.cpp
	
copycons3 : copycons3.o
	${LINK} -o copycons3 copycons3.o ${LINKFLAGS}

copycons31 : copycons31.o
	${LINK} -o copycons31 copycons31.o ${LINKFLAGS}