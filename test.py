
import fileinput
import re

def verifyInvariants(file ):
	i=0
	line = [file.readline() , 0]
	while(True):
		
		line = re.subn('T0(.*?)((TA(.*?)((T6(.*?)T7(.*?)T2(.*?)T3(.*?)T4)|(T5(.*?)T2(.*?)T3)))|((T8(.*?)((T13(.*?)T14(.*?)T9(.*?)T10(.*?)T11)|(T12(.*?)T9(.*?)T10)))))',
		'\g<1>\g<4>\g<7>\g<8>\g<9>\g<10>\g<12>\g<13>\g<16>\g<19>\g<20>\g<21>\g<22>\g<24>\g<25>', line[0].rstrip())
		#print(line[0])
        
		i = i+1
		if(line[1] == 0):
			break
		
	line = re.subn('-' , '' , line[0].rstrip())
	if(line[0] == ""):
		print("El test ha finalizado exitosamente. Enhorabuena!")
	else:
		print("Error de T-Invariantes")
		print(line[0])
	
	#print(i)


file = open("prueba.txt" , "r")

print("Test de invariantes de transicion:")
verifyInvariants(file)

