#!/usr/bin/python
from mrjob.job import MRJob
from mrjob.step import MRStep
import mrjob

#gsm19023 es el campo numero 2
#la media es el campo numero 15
class MRTarea2(MRJob):
	OUTPUT_PROTOCOL=mrjob.protocol.TextValueProtocol
	def mapper_campos(self, _, linea):
		datos = linea.strip().split("\t")
		#no haria falta comprobar pero por si acaso
		if len(datos) == 16:
			try:
				dato = float(datos[2])
			except:
				pass
			else:
				if 100 <= dato <= 1000:
#					yield datos,None
					yield datos,None
			
	def reducer_campos(self,clave,valor):
		yield None,float(clave[15])

	#recibe una lista de pares, devuelve el primer valor de la tupla max
	def reducer_max_media(self,_,valor):
		yield None, str(max(valor))
		
	#dos pasos, en el segundo paso es una lista de pares
	def steps(self):
		return [
			MRStep(mapper=self.mapper_campos,
				reducer=self.reducer_campos),
			MRStep(reducer=self.reducer_max_media)
		]

if __name__ == '__main__':
	MRTarea2.run()

