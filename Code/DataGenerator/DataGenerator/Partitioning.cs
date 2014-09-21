using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace DataGenerator
{
    public class Partitioning
    {
        public List<List<string>> Partitions { get; private set; }

        public Partitioning(int nrPartitions)
        {
            Partitions = new List<List<string>>();
            for (int i = 0; i < nrPartitions; i++)
            {
                Partitions.Add(new List<string>());
            }
        }

        public void WriteToDisk(string path, string extension)
        {

            string folderPath = Path.Combine(path, "P" + Partitions.Count);

            if (!Directory.Exists(folderPath))
            {
                Directory.CreateDirectory(folderPath);
            }

            for (int i = 0; i < Partitions.Count; i++)
            {
                List<string> partition = Partitions[i];
                string filePath = Path.Combine(folderPath, "P" + i + extension);
                File.WriteAllLines(filePath, partition.ToArray());
            }
        }
    }
}
