import matplotlib.pyplot as plt
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

plt.plot(buffer1,'b-',buffer2,'r-')
plt.ylabel('Cantidad en el buffer')
plt.xlabel('Numero de muestra')
plt.yscale('linear')
plt.show()

archivo = open("log2","w")
archivo.write(" ".join(buffer1))
archivo.write("\n\n")
archivo.write(" ".join(buffer2))
