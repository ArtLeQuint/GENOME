# Genome

Statistics computed from NCBI gene bank.

## Travis

[![Build Status](https://travis-ci.com/ResnaHF/Genome.svg?branch=master)](https://travis-ci.com/ResnaHF/Genome)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- [apache poi](https://poi.apache.org/) : A Java API for Microsoft Documents.

## Configuration:

- An option.ini file that allows the user to set a number of parameters:
	- s_SAVE_GENE = false: set a true to save the genes of organisms
	- s_DOWNLOAD_STEP_ORGANISM = 100000: Value of the download step of organisms
	- s_SERIALIZE_DIRECTORY = Save: Name of the serialization directory for java
	- s_TOTAL_PREFIX = Total_: Prefix of excel files for a family (subgroup or group or kingdom)
	- s_EXCEL_EXTENSION = .xlsx: Extension of the statistics files
	- s_GENBANK_NAME = Genbank: Name of the tree root for backup
	- s_KINGDOM_SERIALIZATION_PREFIX = K_: Prefix allowing to parse faster (indicates the prefix of the kingdom)
	- s_SUBGROUP_SERIALIZATION_PREFIX = SG_: Prefix allowing to do parsing more quickly (indicates the subgroup prefix)
	- s_DATEMODIF_SERIALIZE_EXTENSION = - DATEMODIF -. ser: Extension for the files of serialization of the dates of updates of the organisms
	- s_MAX_THREAD = 32: Maximum number of threads to use (will never exceed 4 times the core number of the machine, whatever the value indicated)
	- s_GENE_EXTENSION = .txt: Extension of the files of safeguard of the genes of the organisms
	- s_GROUP_SERIALIZATION_PREFIX = G_: Prefix allowing to do parsing more quickly (indicates the prefix of the group)
	- s_SAVE_GENOME = false: Set to true to save the genome of organisms
	- s_GENOME_DIRECTORY = Genomes: Genomes backup directory name
	- s_CDS_BASE_URL = https \: //www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi: Base URL to download a CDS
	- s_GENE_DIRECTORY = Genes: Name of the backup directory of the genes of the organisms
	- s_DOWNLOAD_CONNECTION_TIMEOUT = 30000: Timeout for download
	- s_RESULT_DIRECTORY = Results: Name of the result directory of the excel files
	- s_DATABASE_SERIALIZATION_PREFIX = D_: Prefix allowing to do parsing more quickly (indicates the prefix of the database)
	- s_ORGANISM_BASE_URL = https \: //www.ncbi.nlm.nih.gov/Structure/ngram: URL to download the list of organizations on GenBank
	- s_SERIALIZATION_SPLITER = -: Spliter to parse faster serialization
	- s_SUM_PREFIX = Sum_: Prefix uses for sums in excel files
	- s_MUTEX_FILE_NAME = locker.mutex: Name of application lock file to prevent multiple simultaneous executions
	- s_GENOME_EXTENSION = .txt: Extension of genome files
	- s_ORGANISM_SERIALIZATION_PREFIX = O_: Prefix allowing to do parsing more quickly (indicates the prefix of organisms)
	- s_SERIALIZE_EXTENSION = -. ser: Extension of the serialization files

- This file is read at the beginning of the execution and is used for the entire program life. If it does not exist (if it is deleted by mistake), it is regenerated when the application is stopped with the default values.

## Files and directories used by the program

- Results: directory of excel files
- Save: directory of the serialization files
- options.ini: user configurable options file
- log.txt: file containing the complete logs generated during the execution
- locker.mutex: Application lock file to prevent multiple simultaneous executions. A special window is displayed when an attempt is made.

## Display of the interface:

- Tree: Displays in real time the file tree with a color code
	- white: files / folders currently registered
	- orange: file / folders being updated
	- blue: files / folders that are being downloaded
	- green: files / folders whose download and updates are completed
- A progress bar that shows the progress of the download based on the number of organizations. The total does not count duplicates and organisms that do not have NC_ replicons.
- Logs: Display of the main logs for the user
- Information: Display of data for an organism or a selected family in the tree

## Using the interface:

- A button "start the download". Once launched, becomes a pause button.
- A "pause" button that sends a pause request to all program download / calculation threads to pause them. This button then becomes a reminder button
- A "restart" button to wake the threads in pause and resume the progress of the application.
- A "stop" button to stop the execution of the program at any time. Sends a shutdown request to all program download / calculation threads. The program waits for the termination of the current actions to guarantee a stable state of the files.

## Authors

* **MANCIAUX Romain** - *Initial work* - [PamplemousseMR](https://github.com/PamplemousseMR).
* **HANSER Florian** - *Initial work* - [ResnaHF](https://github.com/ResnaHF).
* **HARTMEYER Vincent** - *Initial work* - [Sayux](https://github.com/Sayux).
* **FETAIMIA Sami** - *Initial work*.
* **DELRUE Arthur** - *Initial work* - [ArtLeQuint](https://github.com/ArtLeQuint).
* **TEULE Romain** - *Initial work* - [romainTeule](https://github.com/romainTeule).
* **MENANTEAU Adele** - *Initial work* - [Amistery](https://github.com/Amistery).

## License

This project is licensed under the GNU Lesser General Public License v3.0 - see the [LICENSE.md](LICENSE.md) file for details.
