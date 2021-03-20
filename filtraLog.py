import matplotlib.pyplot as plt
import numpy as np
import re
buffer1 = []
buffer2 = []

for i in open("log.txt","r"):
   
   algo = i.find("tiene ")
  
   if(algo>0):
     aux = i[algo+6:]
     buffer1.append(aux[:aux.index(" elementos")])
     aux2 = aux[(aux.find("tiene ")+6):]
     buffer2.append(aux2[:aux2.index(" eleme")])

archivo = open("log2.txt","w")
archivo.write(" ".join(buffer1))
archivo.write("\n\n")
archivo.write(" ".join(buffer2))


     
buffer1 = list(map(lambda x: int(x),buffer1))
buffer2 = list(map(lambda x: int(x),buffer2))


x = np.arange(0., len(buffer1), 1.)
plt.plot(x,buffer1,'b-')
plt.plot(x,buffer2,'r-')
plt.ylabel('Cantidad en el buffer')
plt.xlabel('Numero de muestra')
plt.yscale('linear')
plt.grid(True, which='both')
plt.axhline(y=0, color='k')
plt.axvline(x=0, color='k')
plt.show()


