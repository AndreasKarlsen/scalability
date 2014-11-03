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
        public static string extension = ".txt";

        static void Main(string[] args)
        {
            var random = new Random((int)DateTime.Now.Ticks);
            var lines = new string[5];
            for (int i = 0; i < lines.Length; i++)
            {
                var lineList = new string[100];
                for (var j = 0; j < 100; j++)
                {
                    lineList[j] = random.Next(100).ToString();
                }
                lines[i] = string.Join(",", lineList);
                if (i % 100000 == 0)
                {
                    Console.WriteLine(i);
                }
            }

            if (!Directory.Exists(path))
            {
                Directory.CreateDirectory(path);
            }

            
            File.WriteAllLines(Path.Combine(path,"staticdatameans"+extension), lines);

            /*
            List<Partitioning> partitionings = new List<Partitioning>();
            Console.WriteLine("Creating partitionings:");
            Parallel.For(2, 11, i =>
            {
                Partitioning partitioning =  Partition(lines, i);
                lock (lock_obj)
                {
                    partitionings.Add(partitioning);
                }
                Console.WriteLine("Partition " + i + " done.");
            });

            Console.WriteLine("Writing partitionings to disk:");
            for (int i = 0; i < partitionings.Count; i++)
            {
                Partitioning partitioning = partitionings[i];
                partitioning.WriteToDisk(path, extension);
                Console.WriteLine((i+1)+"/"+partitionings.Count+" done.");
            }
            */
            Console.WriteLine("Done. Press any key to exit.");
            Console.ReadKey();
        }


        public static Partitioning Partition(string[] lines, int nrPartitions)
        {
            Partitioning partitioning = new Partitioning(nrPartitions);

            for (int i = 0; i < lines.Length; i++)
            {
                int partitionIndex = i % nrPartitions;
                partitioning.Partitions[partitionIndex].Add(lines[i]);
            }

            return partitioning;
        }
    }
}
