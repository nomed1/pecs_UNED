#!/usr/bin/python
from mrjob.job import MRJob
from mrjob.step import MRStep
import mrjob

#tomar los 16 primeros elementos y sobre escribiremos los 3 ultimos
#gsm19023 es el campo 2
#gsd19024 es el campo 3
#gsd19025 es el campo 4
#gsd19026 es el campo 5
#gsm19023 sera el campo 13 para el maximo
#gsm19024 sera el campo 14 para el minimo
#gsm19025 sera el campo 15 para la media

class MRTarea1(MRJob):
	OUTPUT_PROTOCOL=mrjob.protocol.TextValueProtocol
	def mapper_campos(self, _, linea):
		datos = linea.strip().split("\t")
		if len(datos) == 26:
			#16 elementos los ultimos 3 se sobreescriben con min,max,avg
			datos = datos[:16]
			yield datos , None
			
	def reducer_campos(self,clave,valor):
		lista=[]
		try:
			for i in clave[2:6]:
				lista.append(float(i))
		except:
			#siguiente clave, podriamos lanzar error,mejor saltar
			pass
			#hay que convertir a string sino la salida da error en float
		else:
			clave[13] = str(max(lista))
			clave[14] = str(min(lista))
			clave[15] = str(sum(lista)/4.0)
			#cadena de salida limpia separada con tabulaciones
			yield None, '\t'.join(clave)
			
	#vamos a hacer el sistema redefiniendo los pasos por si hiciesen falta
	def steps(self):
		return [
			MRStep(mapper=self.mapper_campos,
				reducer=self.reducer_campos)
		]

if __name__ == '__main__':
	MRTarea1.run()
