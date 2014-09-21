using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace DataGenerator
{
    class Program
    {
        public static string path = @"..\..\..\..\Data";
        public static string extension = ".testdata";
        static void Main(string[] args)
        {
            Random random = new Random((int)DateTime.Now.Ticks);
            string[] lines = new string[1000000];
            for (int i = 0; i < 1000000; i++)
            {
                string[] lineList = new string[100];
                for (int j = 0; j < 100; j++)
                {
                    lineList[j] = random.Next(100).ToString();
                }
                lines[i] = string.Join(",", lineList);
                if (i % 100000 == 0)
                {
                    Console.WriteLine(i);
                }
            }

            File.WriteAllLines(Path.Combine(path,"Data"+extension), lines);

            Console.WriteLine("Creation partition:");
            for (int i = 2; i <= 10; i++)
            {
                Partition(lines, i);
                Console.WriteLine("Partition "+i+" done.");
            }

        }


        private static void Partition(string[] lines, int nrPartitions)
        {
            List<List<string>> partitions = new List<List<string>>(nrPartitions);
            for (int i = 0; i < nrPartitions; i++)
            {
                partitions.Add(new List<string>());
            }

            for (int i = 0; i < lines.Length; i++)
            {
                int partitionIndex = i % nrPartitions;
                partitions[partitionIndex].Add(lines[i]);
            }

            string folderPath = Path.Combine(path, "P" + nrPartitions);

            if (!Directory.Exists(folderPath))
            {
                Directory.CreateDirectory(folderPath);
            }

            for (int i = 0; i < partitions.Count; i++)
			{
                List<string> partition = partitions[i];
                string filePath = Path.Combine(folderPath, "P" + i+extension);
                File.WriteAllLines(filePath, partition.ToArray());
			}
        }
    }
}
