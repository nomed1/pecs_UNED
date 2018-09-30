#!/usr/bin/python
from mrjob.job import MRJob
from mrjob.step import MRStep
import mrjob

#gsd19025 es el campo numero 4
class MRTarea3(MRJob):
	OUTPUT_PROTOCOL=mrjob.protocol.TextValueProtocol
	def mapper_campos(self, _, linea):
		datos = linea.strip().split("\t")
		#no haria falta comprobar pero por si acaso
		if len(datos) == 16:
			try:
				gsd19025 = float(datos[4])
			except:
				pass
			else:
				yield datos,None
			
	def reducer_campos(self,clave,valor):
		yield None,float(clave[4])
	
	#recibe una lista , los contamos y asi sabemos el numero de elementos
	def reducer_media(self,_,valores):
		suma=0
		cont=0
		for dato in valores:
			suma = suma + dato
			cont = cont + 1
		yield None, str(suma/cont)
				
	#dos pasos, en el segundo paso es una lista de pares
	def steps(self):
		return [
			MRStep(mapper=self.mapper_campos,
#				combiner=self.combiner_campos,
				reducer=self.reducer_campos),
			MRStep(reducer=self.reducer_media)
		]

if __name__ == '__main__':
	MRTarea3.run()

