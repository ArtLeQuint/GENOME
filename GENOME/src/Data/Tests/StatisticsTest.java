package Data.Tests;

import Data.*;
import Exception.AddException;
import Exception.InvalidStateException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticsTest {

    private static void global(IDataBase _dataBase, long _total, long _totalRep) {
        for (Statistics stat : _dataBase.getStatistics().values()) {
            long totalPhase0 = 0L;
            long totalPhase1 = 0L;
            long totalPhase2 = 0L;
            float totalFreq0 = 0.0F;
            float totalFreq1 = 0.0F;
            float totalFreq2 = 0.0F;
            for (Tuple en : stat.getTriTable()) {
                totalPhase0 += en.get(Statistics.StatLong.PHASE0);
                totalPhase1 += en.get(Statistics.StatLong.PHASE1);
                totalPhase2 += en.get(Statistics.StatLong.PHASE2);
                totalFreq0 += en.get(Statistics.StatFloat.FREQ0);
                totalFreq1 += en.get(Statistics.StatFloat.FREQ1);
                totalFreq2 += en.get(Statistics.StatFloat.FREQ2);
            }
            assertEquals(1.0F, totalFreq0, 0.000001F);
            assertEquals(1.0F, totalFreq1, 0.000001F);
            assertEquals(1.0F, totalFreq2, 0.000001F);
            assertEquals(totalPhase0, stat.getTotalTrinucleotide());
            assertEquals(totalPhase1, stat.getTotalTrinucleotide());
            assertEquals(totalPhase2, stat.getTotalTrinucleotide());
            totalPhase0 = 0L;
            totalPhase1 = 0L;
            totalFreq0 = 0.0F;
            totalFreq1 = 0.0F;
            for (Tuple en : stat.getDiTable()) {
                totalPhase0 += en.get(Statistics.StatLong.PHASE0);
                totalPhase1 += en.get(Statistics.StatLong.PHASE1);
                totalFreq0 += en.get(Statistics.StatFloat.FREQ0);
                totalFreq1 += en.get(Statistics.StatFloat.FREQ1);
            }
            assertEquals(1.0F, totalFreq0, 0.000001F);
            assertEquals(1.0F, totalFreq1, 0.000001F);
            assertEquals(totalPhase0, stat.getTotalDinucleotide());
            assertEquals(totalPhase1, stat.getTotalDinucleotide());
        }
        EnumMap<Statistics.Type, Long> genNumb = _dataBase.getGenomeNumber();
        long totalGenome = 0L;
        for (Statistics.Type t : genNumb.keySet()) {
            totalGenome += genNumb.get(t);
        }
        assertEquals(_total, _dataBase.getTotalOrganism());
        assertEquals(_totalRep, totalGenome);
    }

    private static void global(IDataBase _dataBase, ArrayList<? extends IDataBase> _child, long _total, long _totalRep) {
        long total = 0L;
        long totalValid = 0L;
        long totalOrganism = 0L;
        for (IDataBase child : _child) {
            total += child.getCDSNumber();
            totalValid += child.getValidCDSNumber();
            totalOrganism += child.getTotalOrganism();
        }
        assertEquals(total, _dataBase.getCDSNumber());
        assertEquals(totalValid, _dataBase.getValidCDSNumber());
        assertEquals(totalOrganism, _dataBase.getTotalOrganism());
        assertTrue(_dataBase.getCDSNumber() == _dataBase.getValidCDSNumber() * 2);
        global(_dataBase, _total, _totalRep);
    }

    @Test
    void statistics() throws AddException, InvalidStateException {
        final long nb = 5, nbrep = 200;
        DataBase db = DataBase.load("DataBase", _dataBase -> {
            long tot = nb * nb * nb * nb;
            global(_dataBase, _dataBase.getKingdoms(), tot, tot * nbrep);
        });
        db.start();
        for (int k = 0; k < nb; ++k) {
            Kingdom ki = Kingdom.load("Kingdom_" + k, db, _kingdom -> {
                long tot = nb * nb * nb;
                global(_kingdom, _kingdom.getGroups(), tot, tot * nbrep);
            });
            ki.start();
            for (int g = 0; g < nb; ++g) {
                Group gr = Group.load("Group_" + g, ki, _group -> {
                    long tot = nb * nb;
                    global(_group, _group.getSubGroups(), tot, tot * nbrep);
                });
                gr.start();
                for (int s = 0; s < nb; ++s) {
                    SubGroup su = SubGroup.load("SubGroup" + s, gr, _subGroup -> global(_subGroup, _subGroup.getOrganisms(), nb, nb * nbrep));
                    su.start();
                    for (int o = 0; o < nb; ++o) {
                        Organism or = Organism.load("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, su, true, _organism -> {
                            long tot = 1;
                            long total = 0L;
                            long totalValid = 0L;
                            for (Replicon child : _organism.getReplicons()) {
                                total += child.getCDSNumber();
                                totalValid += child.getValidCDSNumber();
                            }
                            assertEquals(total, _organism.getCDSNumber());
                            assertEquals(totalValid, _organism.getValidCDSNumber());
                            assertEquals(1, _organism.getTotalOrganism());
                            assertTrue(_organism.getCDSNumber() == _organism.getValidCDSNumber() * 2);
                            global(_organism, tot, tot * nbrep);
                        });
                        or.start();
                        for (int r = 0; r < nbrep; ++r) {
                            StringBuilder strBuf = new StringBuilder("AAAAAGATAAGCTAATTAAGCTATTGGGTTCATACCCCACTTATAAAGGT");
                            strBuf.append("TATAATCCTTTTCTTTTTAATTAAAAAAATCTCTAATAATATTTTTTTTA");
                            strBuf.append("TTATATTAATTTCAGGAACTTTAATTACCATTTCATCTAATTCCTGATTA");
                            strBuf.append("GGAGCTTGAATAGGATTAGAAATTAATTTACTTTCATTTATCCCCTTAAT");
                            strBuf.append("AAATGAAGGTAAAAAAAATCTAATAACTTCAGAAAGTAGTCTAAAATATT");
                            strBuf.append("TTTTAACTCAAGCTTTCGCTTCTTCAATTTTATTATTTGCTATTATTTTA");
                            strBuf.append("ATAATAATATTTTTTAATGAAAATTGAATAATAAATAATAATTTTAATAA");
                            strBuf.append("TTTATTAATTTTATCTACTTTATTATTAAAAAGAGGAGCAGCTCCTTTTC");
                            strBuf.append("ATTTTTGATTCCCAGGAGTTATAGAAGGATTAAATTGAATTAATGGTTTA");
                            strBuf.append("ATTTTAATAACTTGACAAAAAATCGCTCCTTTAATACTTATTTCCTATAA");
                            strBuf.append("TTTAAATATTAATTTTTTTTACTTTACAATTTTACTTTCAATAATTATTG");
                            strBuf.append("GAGCTTTAGGAGGATTAAATCAAACATCTTTACGTAAATTAATAGCTTTT");
                            strBuf.append("TCATCAATTAATCATATTGGTTGAATATTAATAGCTATAATAAATAACGA");
                            strBuf.append("ACTTTTATGATTAACTTATTTTTTATTATATTCAATTTTATCTATATCTA");
                            strBuf.append("TCATTTTAATATTTAATAATTTTAAATTATTTCATTTTAATCAAATTTTT");
                            strBuf.append("AATTTTTCAATAATAAATCCTTACATTAAATTCTTTATATTTTTAAATCT");
                            strBuf.append("TTTATCTTTAGGAGGATTACCCCCATTTCTAGGATTTTTACCTAAATGAT");
                            strBuf.append("TAGTTATTCAAAATTTAGTAGAAATAAATCAATTATTTCTTTTATTTATT");
                            strBuf.append("GCTGTTTGTTTAACTTTAATTACTTTATACTATTATTTACGAATATCTTA");
                            strBuf.append("TAGTATTTATATATTAAATTACAATAAAAATTCATGAATATTAATAAATT");
                            strBuf.append("CTTATTCTAATAATAATTTAACTTTAATTTTAACTATAAATTTTATTTCT");
                            strBuf.append("ATTATAGGATTATTAATTATTACATTAATTTATTTAATCTTATAATAAGA");
                            strBuf.append("ATTTAAGTTAAATAAACTAATAGCCTTCAAAGCTGAAAATATTTGTATTA");
                            strBuf.append("ATCTTTTAATTCTTAAACTTTAATAATTTTTTAATTATTCCTTTAGAATT");
                            strBuf.append("GCAGTCTAATATCATTATTGAATATAAAGTTTGATTAAAAAGAATATTTT");
                            strBuf.append("TCTTATATATAAATTTACAATTTATCGCCTAAACTTCAGCCATTTAATCG");
                            strBuf.append("CGACAATGGTTATTTTCAACAAATCATAAAGATATTGGAACTTTATATTT");
                            strBuf.append("CATTTTTGGAGTATGATCTGGAATAGTCGGAACTTCTCTAAGAATTTTAA");
                            strBuf.append("TTCGTGCTGAACTTAGCCACCCTGGTATATTTATTGGGAATGACCAAATT");
                            strBuf.append("TATAATGTAATTGTAACAGCTCATGCATTTATTATAATTTTCTTTATAGT");
                            strBuf.append("AATGCCAATTATAATTGGAGGATTTGGAAATTGATTAGTTCCTTTAATAT");
                            strBuf.append("TAGGAGCCCCTGATATAGCTTTCCCTCGAATGAATAATATAAGTTTTTGA");
                            strBuf.append("ATACTACCTCCTTCATTGACTCTTCTATTATCAAGCTCAATAGTAGAAAA");
                            strBuf.append("TGGGGCAGGAACTGGGTGAACAGTTTATCCTCCTCTCTCTTCAGGAACAG");
                            strBuf.append("CTCATGCTGGAGCTTCTGTTGATTTAGCTATTTTTTCTCTTCATTTAGCT");
                            strBuf.append("GGAATTTCCTCAATTTTAGGGGCAGTAAATTTTATTACAACTGTGATTAA");
                            strBuf.append("TATGTGATCGTCAGGGATTACTTTAGATCGACTACCCTTATTTGTTTGAT");
                            strBuf.append("CTGTAGTTATTACAGCTATCTTATTACTTCTTTCTCTTCCTGTTTTAGCT");
                            strBuf.append("GGAGCTATTACTATATTATTAACAGACCGAAACTTAAATACATCTTTCTT");
                            strBuf.append("TGATCCAATCGGAGGGGGAGACCCTATTTTATACCAACACTTATTTTGAT");
                            strBuf.append("TCTTTGGACACCCAGAAGTTTATATTTTAATTTTACCCGGATTTGGAATA");
                            strBuf.append("ATTTCTCATATTATTACTCAAGAAAGCGGAAAAAAGGAAACATTTGGAAC");
                            strBuf.append("TTTAGGAATAATTTATGCTATATTAACAATTGGATTATTGGGATTTATTG");
                            strBuf.append("TTTGAGCTCATCATATATTTACAGTAGGTATAGACGTAGATACTCGAGCT");
                            strBuf.append("TATTTTACTTCAGCAACTATAATTATTGCTGTTCCTACAGGAATTAAAAT");
                            strBuf.append("TTTTAGTTGATTAGCAACTTTACACGGAACTCAATTAACATATAGTCCAG");
                            strBuf.append("CCCTTCTATGATCATTAGGATTTGTATTTTTATTTACAGTTGGAGGTTTA");
                            strBuf.append("ACAGGAGTAGTATTAGCTAATTCTTCAATTGATATTGTTCTTCATGATAC");
                            strBuf.append("TTATTACGTAGTTGCCCATTTTCATTACGTTTTATCTATAGGAGCTGTAT");
                            strBuf.append("TTGCTATTATAGCAGGATTTATTCATTGATACCCTTTATTAACAGGAATA");
                            strBuf.append("GTTATAAACCCTTCATGATTAAAGGCTCAATTTAGTATAATATTTATTGG");
                            strBuf.append("AGTAAATCTAACTTTCTTTCCTCAACATTTTTTAGGGTTAGCTGGAATAC");
                            strBuf.append("CTCGACGATACTCAGATTTTCCTGATAGCTACTTAACTTGAAATATTATT");
                            strBuf.append("TCTTCTTTAGGAAGAACAATTTCACTATTTGCCGTTATTTTCTTTTTATT");
                            strBuf.append("TATTATTTGAGAAAGTATAATTACTCAACGAACACCTTCTTTCCCTATAC");
                            strBuf.append("AATTATCTTCATCTATTGAATGATATCATACACTTCCTCCTGCAGAACAT");
                            strBuf.append("ACTTATTCAGAATTACCACTACTTTCTTCTAATTTCTAATATGGCAGATT");
                            strBuf.append("AGTGCAATGAATTTAAGCTTCATATATAAAGATTATTATCTTTTATTAGA");
                            strBuf.append("AAATGGCAACATGAGCAAATTTAGGACTTCAAAACAGTTCATCCCCTTTA");
                            strBuf.append("ATAGAACAATTAAATTTTTTTCATGATCATACTTTATTAATTCTAATTAT");
                            strBuf.append("AATTACAGTTATAATTGCATATATTATATTTATATTATTTTTTAACAAAT");
                            strBuf.append("TTACAAATCGATATTTATTACACGGACAAACAATTGAAATTATCTGAACA");
                            strBuf.append("ATTTTACCCGCTATTATTTTAATATTTATTGCCTTCCCTTCATTACGACT");
                            strBuf.append("ATTATACTTAATAGACGAAATTAATTCACCATTAATTACTTTAAAGGTTA");
                            strBuf.append("TTGGTCACCAATGATATTGAAGTTATGAATACTCAAATTTTTTAAATTTA");
                            strBuf.append("GAATTTGACTCTTATATAATTCCTACCAACGAATTAGATTTAAATGGATT");
                            strBuf.append("CCGTTTATTAGATGTTGATAACCGAATTATTTTACCTATAAATAATCAAA");
                            strBuf.append("TTCGTATTTTAGTTACAGCTACAGATGTTCTTCATTCCTGAACAGTCCCC");
                            strBuf.append("TCTTTAGGAGTTAAAATTGATGCTACTCCAGGACGATTAAACCAAACTAA");
                            strBuf.append("TTTTTTAATAAATCAACCTGGATTATTCTTTGGTCAATGCTCAGAAATCT");
                            strBuf.append("GTGGAGCAAATCATAGTTTTATACCAATTGTTGTTGAAAGAATTCCTATA");
                            strBuf.append("AACTATTTTATCAAATGAATTTCTTCTCAAATAAATTCATTAGATGACTG");
                            strBuf.append("AAAGCAAGTAATGATCTCTTAAATCATATTATAGTAAATTAGCACTTACT");
                            strBuf.append("TCTAATGAAAAATATATTTAAACTAAAAAATTAGTTTAACCCAAAACCTT");
                            strBuf.append("AGTATGTCAAACTAAAAAAATTAGTTTAATCTAATATTTTTTAATTCCTC");
                            strBuf.append("AAATAGCCCCTATTAGTTGATTAACTTTATTTTTTGTTTTTTCTATCACA");
                            strBuf.append("TTAGTAATTTTTAATATTAAAAATTATTTTTGTTTTTCTTATAATTCAAC");
                            strBuf.append("TGAAACATCCCAAAACTTGAATATTAAACAACACAAATTAAACTGAAAAT");
                            strBuf.append("GATAACAAACTTATTTTCCGTATTTGACCCTTCAACAACTATCTTAAATT");
                            strBuf.append("TGTCCTTAAATTGACTAAGAACTTTTTTAGGTCTATTAATTATTCCTTCA");
                            strBuf.append("ACTTACTGATTAATACCTAATCGATTCCAAATTATTTGAAATAACATTTT");
                            strBuf.append("ATTAACACTTCACAAGGAATTTAAAACTCTTTTAGGTCCTAATGGACATA");
                            strBuf.append("ATGGAAGAACATTAATATTTGTTTCTTTATTTAGATTAATTATATTTAAT");
                            strBuf.append("AATTTTTTAGGATTATTCCCGTATATTTTTACGAGTACTAGTCATTTAAC");
                            strBuf.append("TTTAACTTTAACTTTAGCTTTCCCATTATGATTAAGATTCATGCTTTATG");
                            strBuf.append("GATGAATTTGTCACACACAACATATATTTGCTCATTTAGTTCCTCAAGGA");
                            strBuf.append("ACTCCTCCTGTTTTAATACCTTTTATAGTATGCATTGAAACTATTAGTAA");
                            strBuf.append("TGTAATCCGACCAGGAACTTTAGCAGTACGATTGACTGCTAATATAATTG");
                            strBuf.append("CAGGACATTTACTAATAACTTTATTAGGAAATACCGGCCCTATATCAACA");
                            strBuf.append("TCTTATATCATTCTTTCATTAATTTTAATTACTCAAATTGCTCTTTTAGT");
                            strBuf.append("TCTTGAATCTGCTGTTGCTATTATTCAATCTTATGTTTTTGCAGTATTAA");
                            strBuf.append("GAACTCTTTATTCTAGTGAAGTAAATTAATTTATGTCAACACATGCAAAT");
                            strBuf.append("CACCCATTTCACTTAGTAGATTATAGCCCTTGACCTTTAACAGGAGCTAT");
                            strBuf.append("CGGAGCTATAACAACAGTTACAGGCCTTGTCCAATGATTTCATCAATATG");
                            strBuf.append("ATAATACTTTATTTTTACTGGGTAATATCATTACTATATTAACTATATAC");
                            strBuf.append("CAATGATGACGAGATATTTCTCGAGAAGGAACTTTTCAAGGACTTCACAC");
                            strBuf.append("TATTCCCGTAACATTAGGATTACGATGAGGAATAATTTTATTTATTATTT");
                            strBuf.append("CTGAAGTTTTTTTCTTTATTTCCTTCTTTTGAGCTTTTTTCCATAGTAGC");
                            strBuf.append("TTATCTCCAACAATCGAATTAGGAATAGTTTGACCTCCAATTGGAATTGA");
                            strBuf.append("ACCTTTTAATCCTTTTCAAATTCCTCTTTTAAATACAGCTATTTTATTAG");
                            strBuf.append("CTTCCGGGGTTACAGTAACTTGAGCCCATCATAGTTTAATAGAAAATAAT");
                            strBuf.append("CATACTCAAACAATTCAAAGATTATTTTTTACTGTTCTTTTAGGAATTTA");
                            strBuf.append("TTTTTCAATTCTTCAAGCTTATGAATATATTGAAGCTCCTTTTACAATTG");
                            strBuf.append("CCGATAATGTTTATGGATCTACATTTTTTGTAGCAACAGGATTTCACGGA");
                            strBuf.append("CTACACGTATTAATTGGAACTTCTTTTCTATTAATTTGTTTATTTCGACA");
                            strBuf.append("TATAAATTGTCATTTTTCTAGAAGTCATCATTTTGGATTCGAAGCAGCTG");
                            strBuf.append("CTTGATATTGACATTTTGTTGATGTAGTTTGACTATTCTTATATATTTCA");
                            strBuf.append("ATTTATTGATGAGGTAATTAATTTATAAAGTATATAATTGTATATGTGAC");
                            strBuf.append("TTCCAATCACAAGGACTAAATAATTTTAGTATAAATAATTATTATATTAT");
                            strBuf.append("TCATTATAAGAATTATTATTTTTACTATTACAATTATTGTAATAATATTA");
                            strBuf.append("GCAACAATTTTATCAAAAAAAACTATCACAGATCGAGAAAAATCATCTCC");
                            strBuf.append("ATTTGAATGTGGATTCGACCCAATAAATTACTCTCGTTTACCTTTTTCAT");
                            strBuf.append("TACGATTTTTTTTAATTGCTATTATTTTTTTAATTTTTGATGTAGAAATT");
                            strBuf.append("GCTTTAATCCTACCAATAATTTTAATTATTAAAACATCAAATCTAATAAA");
                            strBuf.append("TTGATCAATGACTAGTTTATTTTTTATTTTCATTTTATTAATTGGCTTAT");
                            strBuf.append("ACCATGAATGAAACCAAGGAGCTTTAGAATGAAATAATTAAAATATGAAG");
                            strBuf.append("CGATTTATTGCAATTAGTTTCGGCCTAATCTTAGGTGAAATTCACCCATA");
                            strBuf.append("TTTTGGGATAATAGTTAACTATAACATTTAATTTGCATTTAAAAAGTATT");
                            strBuf.append("GAATTTATATTCAATTTATCTTAATTAATTGAAACCAAAAAGAGGTATAT");
                            strBuf.append("CACTGTTAATGATATCATTGAATAATTTATATATTCCAATTAAGTAGAAA");
                            strBuf.append("TATAAATGGAATTAAACCATTAAAGATAAAAGTTAGCAGCTTTTTCTTGA");
                            strBuf.append("TCAACATATTTCATTTTTAATTCCATTTATATAGTTTAAAAAAAACATTA");
                            strBuf.append("CATTTTCACTGTAAAAATAAAAATTTATTTTTTATAAATATTTAAAAATT");
                            strBuf.append("ACAATAATTTCCCTAACATCTTCAGTGTCATGCTCTAAATATAAGCTATT");
                            strBuf.append("TAAATTTAAAAATTAATTTAAAAATAATATTATTCTTACTAAAATAATTA");
                            strBuf.append("CTCATAATATATAACTTAATAAATAAATTTTTAAATTATTATTTTGAAAT");
                            strBuf.append("TCTTGAACATATAATGAATAAATTTTTAATTGATTATATAATATCTGACC");
                            strBuf.append("TCCAAAATATTCACTTCATCCTTGATCAAACATTTTATAAGAATATATTC");
                            strBuf.append("CTAAAATTAATGGTCATTTTACAACACCTACAGTTGAAATTAAAGGTATA");
                            strBuf.append("AATCACATACTTCCAGAAAAAAAACTAAAATTATAATAAACTAAAGACTT");
                            strBuf.append("ATTAATAAAAAAAAAATTAACATTTCTAATTAAATACCCAGTAAATCCTC");
                            strBuf.append("CTAATAAACAAACTATTAATGTTAAATTTTTTAAATAATAAGGTAAACAA");
                            strBuf.append("ATTATTTCAGGATTAAAAAATATTAATCAACTTAACATTCTTCCCCCAAT");
                            strBuf.append("AATAGCTATAACTATTAAAAAAAAAATTCTAAAAGATATAGTTCACCCCT");
                            strBuf.append("TATCATTTAACATATTCAATGTAGTTCTATTAAAATCCCCTGTTATTGAA");
                            strBuf.append("TAATAAACTAATCGAAAAGAATAACATACAGTTAACCCTGTAGAAAAAAA");
                            strBuf.append("AAAAAGAAAAAAAGAAAAAAAATTTATATAAGACAACATTACTGTTTCTA");
                            strBuf.append("AAATTAAATCCTTCGAATAAAACCCCGCCAAAAAAGGTATTCCACATAAA");
                            strBuf.append("GCTAAATTAGCAATATTAAAACAACTACATGTTAAAGGTATTCTTATACT");
                            strBuf.append("TAAACCCCCTATAAATCGAATATCTTGAGCATTTTTTGTATTATGAATAA");
                            strBuf.append("TTACTCCAGCACATATAAATAATAAAGCCTTAAAGAGTGCATGTGTTAAT");
                            strBuf.append("AAATGAAAAAAAGCTAACTTATAATAACCAATAGATAAAATACTTATTAT");
                            strBuf.append("TAAACCTAATTGACTTAAAGTAGATAAAGCAATAATTTTTTTTAAATCAA");
                            strBuf.append("ACTCAAAATTAGCCCCTAATCCAGCTATAAATATTGTTAATCCAGAAACT");
                            strBuf.append("AATAATAAAAATTGTCCTAACTTAGAATTATCTAATAAAATATTAAATCG");
                            strBuf.append("AATTAATAAATAAACTCCAGCAGTTACTAATGTAGAAGAATGTACTAAAG");
                            strBuf.append("CAGAAACAGGAGTCGGTGCTGCCATAGCTGCAGGTAATCAAGAAGAAAAA");
                            strBuf.append("GGAATTTGAGCACTTTTAGTCATTGCAGCTAATATTACTAATCCTCCAAT");
                            strBuf.append("AACTATTATTTCAAAATTATTTTTTATTATATCTAAATAAAAAATATAAT");
                            strBuf.append("TTCAACTTCCATAATTTAATATTCAAGCAATAGCTAATAATAAAGCAACA");
                            strBuf.append("TCCCCGATTCGATTAGATAAAGCTGTTAATATACCAGCATTATAAGATTT");
                            strBuf.append("TACATTTTGAAAATAAATTACTAAACAATAAGAAACTAATCCTAACCCAT");
                            strBuf.append("CTCATCCTAATAAAATTCTAATTAAATTAGGACTAATAATTAATATTATT");
                            strBuf.append("ATTGACATAACAAATATTAATACTAATAAAATAAACCGATTTACATTATA");
                            strBuf.append("ATCTTCTCCTATATATTGGTCTCTATAAAAAATTACTAAAGAAGAAATTA");
                            strBuf.append("ATAAAACAAAAGATATAAATATTAAACTTATTCAATCAAATAAAAAAGTT");
                            strBuf.append("ATTACAATAGATATTGAATGTAAAGAAACAATTTCTCATTCAATAAAATA");
                            strBuf.append("TACTAAATCTATTAATAAAAACTTTAATCTTAAAATAAATAAAGTAAATC");
                            strBuf.append("TAATAAAAATTAAAATATAAAAACTATTTTTACAATAATTAACTAAATAA");
                            strBuf.append("TTCACGATCTAAAATGAAATTTTCATATCATTGACACCACAAATCAATAT");
                            strBuf.append("TTTATTTTAAACTATTTAAATTAAAATTAAATTCATAATATACAAAAATT");
                            strBuf.append("TCTTTTTATAATTAATAAATTTAATGGTAATCAATGCAATATTAATACTA");
                            strBuf.append("AAAATTCTCGAGTAGTTCCTGAAGAAAAAAAATAAACCCCTGAATAAATT");
                            strBuf.append("TTTCCATGTTGTCTATAAGCAAATAAATATAAAGTATAAGCTGCTCTAAA");
                            strBuf.append("AAAAGACAAAAAAGCTAAACTAATTATAGTTAATCAAGATCAACTAACAA");
                            strBuf.append("TTCTATTTAATAAAGAAATTTCTCCTAATAAATTTAAAGTAGGTGGAGCT");
                            strBuf.append("GCCATATTCCCAGAACATAATAAAAATCATCATAAAGATAAACTAGGCAT");
                            strBuf.append("AAAATTTAATATCCCTTTATTAATTAATAAACTTCGTCTTCCTATTCGTT");
                            strBuf.append("CATAAGAAATATTAGCCAAACAAAATAACCCAGAAGAACATAACCCATGA");
                            strBuf.append("GCAATTATTAAAGTATATGACCCATTTAATCCCCAATAAGTTATTGTTAT");
                            strBuf.append("TAATCCTCTTAATACGATCCCTATATGAGCAACCGAAGAATAAGCAATTA");
                            strBuf.append("AAGCCTTTAAGTCTATTTGTCATAAACAAATTAAACTAACTAAAACTCCC");
                            strBuf.append("CCAATTAAGCTAATACTAATTCAAATATAATTAAATTTTATACCTAAAAC");
                            strBuf.append("TTGCATTAAAGAAAATACCCGTAATAAGCCATACCCTCCTAATTTTAATA");
                            strBuf.append("AAACACCTGCCAAAATTATAGACCCTGAGACAGGGGCTTCTACATGAGCC");
                            strBuf.append("TTTGGTAGTCATAAATGAACTAAAAATATTGGCATTTTTACTAAAAAAGC");
                            strBuf.append("AAAAATTATACATAAATATAAAAAATCTAAATTATATAAATTTTTATAAC");
                            strBuf.append("TTAATATAATAAAATTCATTGTATAATTATCATTTTTAATATAAAAAATA");
                            strBuf.append("CCAATTAATAACGGCAACGAAGCCAGTAAAGTATAAAATAATAAATAAAT");
                            strBuf.append("CCCCGCCTGTAAACGTTCAGGTTGATATCCTCAACCTAAAATTAAAAATA");
                            strBuf.append("AAGTTGGAATTAGACTTCTTTCAAAAAATAAATAAAATATAAATATACTT");
                            strBuf.append("ATGGAACTAAAAGTAAAAATTAATATTAAAAGTAAAAAAACAATTATAAA");
                            strBuf.append("TAAAAATAAATTAACATAATTATTATAACGAACTACCCCTTCTCTAGCTA");
                            strBuf.append("TTAATATTAAACCACAAATTCAAAAACTTAATAAAATTAATCCATAAGAA");
                            strBuf.append("ATTATATCTATACCAAAATAATAAGAAATATCACAAAAATAATAATTAGA");
                            strBuf.append("ACTAATTTTAATTATAAATAAACAAGTAGCTAAAAAAATTAAATTTTGAA");
                            strBuf.append("CCATTCAATAAATATTTTTTTTAAAAAATAAAAAAAAAATAAATATAATT");
                            strBuf.append("ATAAAAATAAATTTTAACATTGTAAAATAGAAAAACTCTGAAAATAATCA");
                            strBuf.append("TTACCATGAGTTCGAATTATAGAAACTAAAATGGATAGCCCTAGAACCCC");
                            strBuf.append("TTCACAAACACAAAAAGTTAAAAAAAATATACTAAAGTAACTTTCATAAT");
                            strBuf.append("TTATAAAATTTAAATACAAAAATAGTAACATAAATAATATTAATACTATA");
                            strBuf.append("AACTCTAATCTCAATAAAGTACAAAGTAAATGCTTACGACTAGAAATAAA");
                            strBuf.append("AACAATACTTCCAAAAATAAATATAATAATTATTAAATAATATATATATA");
                            strBuf.append("AATTTATCATTAGTTTTAATAGTTTAAAAAAAACATTAGTCTTGTAAACT");
                            strBuf.append("AAAAATAAAAATTATTTTTTTTAAAACTTCAAGAAAAAAGATACTTCCTT");
                            strBuf.append("TTCATTAACTCCCAAAGTTAATATTTTATTATAAACTATTTCTTGATATT");
                            strBuf.append("ATAACATTAATTATACTAATTTCTTTAATTACTAGTTTTATCTTTATACA");
                            strBuf.append("AATAAAACACCCCTTAGCTATAGGGTTAATACTTTTAATTCAAACATTTC");
                            strBuf.append("TAACTTCCCTGTTAACTGGTATATTCGTAAAAACATTTTGATTTTCTTAT");
                            strBuf.append("GTTTTATTTTTAATTTTTATAGGAGGAATATTAGTTTTATTCATTTATGT");
                            strBuf.append("AACTTCTCTTTCATCTAATGAAATATTTTCATTATCTATAAAACTATTTT");
                            strBuf.append("TTTTATCTTTAAGTATAATTTTAATATTTATTGTATTTTCTTTTTTTTTT");
                            strBuf.append("GATAAATCAATTATTAGAATATTTATTAATAATAATGAAATAAATAATTT");
                            strBuf.append("ATTTTCTACAAATTCTTTAATAATAGAAGACTTAATTTCATTAAATAAAA");
                            strBuf.append("TATATAACTTTCCAACTAATTTAATTACTTTATTATTAATTAATTATTTA");
                            strBuf.append("TTTTTAACTTTATTAGTAACAGTAAAAATTACAAAAAAAAATTATGGGCC");
                            strBuf.append("CTTACGCCCAATAAATTAATTTATGTCTAAATCATTACGAAAAACCCACC");
                            strBuf.append("CTTTATTAAAAATAGCAAATAATGCTCTTGTCGATCTCCCGGCTCCTTCC");
                            strBuf.append("AATATTTCTGCTTGATGAAATTTTGGTTCTTTATTAGGATTATGTTTAAT");
                            strBuf.append("CATCCAAATTTTAACAGGATTATTCTTAGCAATACACTATACTGCTGATA");
                            strBuf.append("TTGAAACAGCTTTTAATAGAGTAAATCACATTTATCGTGATGTTAATAAT");
                            strBuf.append("GGTTGATTCCTACGAATTTGTCATGCTAATGGAGCTTCTTTTTTTTTCGC");
                            strBuf.append("TTGTTTATTTATTCACGTAGGACGAGGAGTTTATTATAATTCATACTTAT");
                            strBuf.append("ATATTCCAACATGAATAATTGGAGTAATTATTTTATTTATAGTAATAGCA");
                            strBuf.append("ACAGGATTTTTAGGATATGTTCTACCTTGAGGGCAAATATCATTCTGAGG");
                            strBuf.append("AGCCACAGTAATTACTAATCTTTTATCCGCTGTACCTTATTTAGGTACAG");
                            strBuf.append("ATTTAGTTCAATGAATTTGAGGAGGATTCGCTGTTGATAATGCTACTTTA");
                            strBuf.append("ACTCGATTCTTTACATTTCATTTTATTCTCCCTTTCATTGTATTAGCTTT");
                            strBuf.append("AACTATAATTCATCTATTATTTTTACACCAAACAGGATCAAATAATCCAT");
                            strBuf.append("TAGGGTTAAATTCAAATGTAGATAAAATTCCCTTTCATCCTTATTTTGTT");
                            strBuf.append("TACAAGGATATTGTTGGATTTATCATTTTTATATGAATTTTAATTGGATT");
                            strBuf.append("TATTTGAAAATTCAATTACTTACTAATAGACCCAGAAAATTTTATTCCAG");
                            strBuf.append("CTAACCCATTAGTTACTCCTGTCCATATTCAACCAGAATGATATTTTTTA");
                            strBuf.append("TTTGCTTATGCAATTTTACGTTCTATTCCAAATAAATTAGGAGGAGTAAT");
                            strBuf.append("TGCATTAGTTTTATCTATTGCAATTTTAATAATTCTCCCATTTACTCATA");
                            strBuf.append("CTAGTAAATTCCGAGGATTACAATTCTATCCTTTAAATCAAATTTTATTT");
                            strBuf.append("TGAAATATAGTAATTGTGGCTTCCCTTTTAACTTGAATTGGAGCTCGCCC");
                            strBuf.append("TGTTGAAGATCCTTATGTTTTAACAGGACAAATTCTAACAGTATTATATT");
                            strBuf.append("TTTCTTATTTTATTATTAATCCTTTAATATCAAAATATTGAGATAAATTA");
                            strBuf.append("TTAAATTAATTAATAAGCTTTTATAGCATATGTCTTGAAAACATAAGAAA");
                            strBuf.append("GAAGTTAAACCTTCTATTAATTTAATTTTATACTAAAAATTATTCATTAA");
                            strBuf.append("AATAATATAGATAAAATAAAAATTTTTACCCCAATAAAAAAAAATAAATA");
                            strBuf.append("ATTTAAAGACATCGGTAAAAAACTCTTTCAAGCTAAATACATTAATTTAT");
                            strBuf.append("CATACCGAAACCGAGGTAAAGTCCCTCGAACCCAAATAAAAAAAAAAGAA");
                            strBuf.append("ATAATAGTTAATTTAAAAAAAAATAAAATTCTATAAATATCTCTACCTAA");
                            strBuf.append("AAAAATTACTCTAAATAACATACTTATAAATAAAATACTAGAATATTCAG");
                            strBuf.append("CTAAAAAAATTAAAGCAAATCCCCCTCTTCTATATTCTACATTAAATCCT");
                            strBuf.append("GAAACTAACTCAGACTCCCCTTCAGCAAAATCAAAAGGGGTACGATTAGT");
                            strBuf.append("TTCAGCTAAACAAGATGCAAATCAAACGAGACCTAAAGGAAAACAAAATA");
                            strBuf.append("CAATAAATCATATATATTTCTGATATATAAAAAAATTCAAAAAATTATAA");
                            strBuf.append("TTTCCAATTAAAAAAATAAATCTCAATAAAATTAAAGCCAATCTAACTTC");
                            strBuf.append("ATAAGAAATAGTTTGTGCAACAGCCCGTAATCCTCCTAATAAAGCATAAT");
                            strBuf.append("TAGAATTAGAAGATCACCCCGCCACTATTACGGTATAAACTCCTAATCTA");
                            strBuf.append("GTAATACATAAAAAAAATAAAACTCCTAAATTAAAAGAATACAATTTAAT");
                            strBuf.append("TAAATAAGGCATACTTATTCAAATTAATAAAGACAAAAATAAAGAAAAAA");
                            strBuf.append("TAGGAGAAAAATAATAAAAAATATAATTAGATAATAAAGGATAAGTTTGT");
                            strBuf.append("TCCTTAGTAAATAACTTTACAGCATCACTAAAAGGTTGTAATAACCCTAT");
                            strBuf.append("AAATCCTACTTTATTAGGTCCTTTACGAATTTGAATATATCCTAAAACTT");
                            strBuf.append("TCCGCTCTAATAAAGTTAAAAAAGCTACCCCTACTATTACACAAATTACC");
                            strBuf.append("AATAATAAACTTCCAATCAATGATAATAAATAATCTATATAAAACAATAC");
                            strBuf.append("TATTTATAATTAATTAAATTATATAAATAAATTCTAAATTTATTGCACTA");
                            strBuf.append("ATCTGCCAAAATAGTTTATTAAATATTAATATAATTCATAAATTTAAAAT");
                            strBuf.append("TTATATTTTTTATATTAGGTCCTTTCGTACTATAATATAATAATTAATTA");
                            strBuf.append("AAGATAGAAACCAACCTGGCTTACGCCGGTTTGAACTCAGATCATGTAAG");
                            strBuf.append("AATTCAAAGGTCGAACAGACCTAAACTTTAAACTTCTACACCTAAAAATA");
                            strBuf.append("ACTCTTAATCCAACATCGAGGTCGCAATCTTTTTTGTCGATAAGAACTCT");
                            strBuf.append("AAAAAAAAATTACGCTGTTATCCCTAAGGTAACTTAATTTTTTAATCAAT");
                            strBuf.append("ATAACTGGATCAAATATTCATAAATTAATGTAAATTAAAAATAAAAGTTT");
                            strBuf.append("TTTAAATTTTAATACCACCCCAGTAAAATTTTTTATTAAAATATAAATTT");
                            strBuf.append("AATTATTCTTTTTAATTTATAATTAACAAAAATAAAGATCTATAGGGTCT");
                            strBuf.append("TCTCGTCTTTTAATTACATTTTAACTTTTTAATTAAAAAATAAAATTCTA");
                            strBuf.append("TAAAAATTTAAAAAAGACAGTATATATCTCATTCAACCATTCATACAAGC");
                            strBuf.append("CTTCAATTAAAAGACTACTGATTATGCTACCTTCGCACAGTCAAAATACT");
                            strBuf.append("GCGGCCCTTTAATAATTTATCAGTGGGCAGGTTAGACTTTAAATTAAATT");
                            strBuf.append("CAAAAAGACATGTTTTTGATAAACAGGTGAATATATAATTTGCCGAATTC");
                            strBuf.append("CTTATTTAAACCTTTCATTTTAAATAATTTATATAAATTAAATATACTAA");
                            strBuf.append("TTTTATCATAATTTATAAATTATTATCATTAAAATTTATATTTTAATAAA");
                            strBuf.append("TAATTTAATTTAACAATAAATAATAAAAATTTTAAAATAAATTATAACAA");
                            strBuf.append("ATTTATTAATGATAACTATTTTTAAGCTTACATTTATTAAATATTTTATT");
                            strBuf.append("AAAAATTTAAAAATTTATTTTAAAGCTAATCCCTTAAAATATTAATTTTA");
                            strBuf.append("TAAAATAAAAATACTATATAAATAGAACTTTTAATTTATAAATCTAAATT");
                            strBuf.append("AAATTTATTTCTTAAAAAACTAGATATATTTTAAAACGATTAACATTTCA");
                            strBuf.append("TTTCTAATTATATATTTAAAATAATTATTCCACAGTAACTTTTATATATA");
                            strBuf.append("ATTAAATCTTTAAAATTCGAGAAAAATTAATATAATAATTAATTAATTAA");
                            strBuf.append("TAAACCCTGATACACAAGGTACAATAAATTAAATTTTCTTTTTAAATAAA");
                            strBuf.append("AATTTTTCAAATTATTTCAATTTTCTTTTACAATACAAATATACTATTAT");
                            strBuf.append("TAAAATTATTTATTTCTTTAAAAAATACTAAAACTAAAATTATTAAAATT");
                            strBuf.append("ATTTTTATTAATTAAATTAAAAAAAAAAAAAATTAATAAATAAAATTTAA");
                            strBuf.append("TCAATTTAAATTGATTTGCACAAATTTCTTTTCAATGTAAATGAAATACT");
                            strBuf.append("TTACTAATTAAGCTTTAAATTGTCTTTCTAGATACACTTTCCAGTACATC");
                            strBuf.append("TACTATGTTACAACTTATCTTATTTTAAAAATAAGAACGATGGGCGATAT");
                            strBuf.append("GTACATGTTTTAGAGCTAAAATCATATAAATAATCTATTTTATATTACTA");
                            strBuf.append("TTAAATCCACCTTCAAATTTTTGTTTCAAAAAATTATTCATTTAAATAAA");
                            strBuf.append("TTTATTGTAATCCATCTCTACTTAAATATAAACTGCACCTTGATCTGACA");
                            strBuf.append("TTTTATTTAATAAAATATTAAGAAAATTAAATCTTATAACATATTCTGAT");
                            strBuf.append("GACGACGATATACAAATTAAACAAATTTAAGTAAGGTTCAATGTGAATTA");
                            strBuf.append("TCAATTACAGGACAGATTCCTCTAAATAGACTAAAACACCGCCAAATTTT");
                            strBuf.append("TTAAATTTTAAGAATATAACTAATACTACTTTAGCATTTTAATATTTATT");
                            strBuf.append("TTTAATAATAGGGTATCTAATCCTAGTTTATTACAAAAAGTTTATAGCTT");
                            strBuf.append("AAATTATTTTTTAAATAATAAATTAATAAATTTAAAAATTTCACCTAATA");
                            strBuf.append("AATTTATATTTATATTATAAAGAGTGAATCTGATATTAATAAAGAAGAAA");
                            strBuf.append("TTTTGTATAGTAATAATATAGTAGTATGGTAAATTTTGTGCCAGCTACCC");
                            strBuf.append("GCGGTTATACAAATATGAGCAAGTAAAAATTTTAGTATATAGTTAAATTA");
                            strBuf.append("TAAATTTTCAATTTATAATTTAACTAATACTAAAAATTTTTACTTGCATT");
                            strBuf.append("ATTTGTATAACCGCGGTAGCTGGCACAAATTTTACCAATACTATATATTA");
                            strBuf.append("TTACTAATACAAAATTTCTTTTTTAATTAATATCAATTACTGCGAATAAA");
                            strBuf.append("TTAATTATTATAATTTTTTAAAAATTAAAATAATTCACACAAAAATTTAC");
                            strBuf.append("ATGTAAAATAAAATAATAATAAAAATATTAACCAAAATAAAATAATATTT");
                            strBuf.append("ATTGATATAAATATTAAAAAATACAATTATTATTTTTAAAATAATATAAA");
                            strBuf.append("ATTAATAAAACTTAAAAATTTACAAAATTATTAATTATGTAAAATTAATT");
                            strBuf.append("AACACAAAATTTTATTAGTTCAATTCTCACTAAATTAAATTTTATATAAA");
                            strBuf.append("AATACCCCTTAAATATTTTTATATTTATTTATTAATAAATAATTTTATAC");
                            strBuf.append("CAATTAATCACTATTTTTAAAATAATATAAAATTAATAAAACTTAAAAAT");
                            strBuf.append("TTACAAAATTATTAATTATGTAAAATTAATTAACACAAAATTTTATTAGT");
                            strBuf.append("TCAATCCTCACTAAATTAAATTTTATATAAAAATACCCCTTAAATATTTT");
                            strBuf.append("TATATTTATTTATTAATAAATAATTTTATACCAATTAATCATTATTTTTA");
                            strBuf.append("AAATAATATAAAATTAATAAAATTTAAAAATTTACAATAATTATCAATTA");
                            strBuf.append("TGTAAAATTAATTAACACAAAATTTTATTAGTTCAATCCTCACTAAATTA");
                            strBuf.append("AATTTTATATAAAAATACCCCTTAAATATTTTTATATTTATTTATTAATA");
                            strBuf.append("AATAATTTTATACCAATTAATCATTATTTTTAAAATAATATAAAATTAAT");
                            strBuf.append("AAAATTTAAAAATTTACAAAATTATTAATTATGTAAAATTAATTAATATA");
                            strBuf.append("AAATTTTATTAGTTCAATCCTCACTAAATTAAATTTTATATAAAAATACC");
                            strBuf.append("CCTTAAATATTTTTATATTTATTTATTAATAAATAATTTTATACCAATTA");
                            strBuf.append("ATCATTATTTTTAAAATAATATAAAATTAATAAAATTTAAAAATTTACAA");
                            strBuf.append("AATTATTAATTATGTAAAATTAATTAATATAAAATTTTTATTAGTTTAAT");
                            strBuf.append("TCCAGTTTAATAATTGTAATATATAATATTTTTATAATTATATAATATAA");
                            strBuf.append("TTATTAATAAATATTTACTATAATTATTAAATATTTTTAGTTTTCCCTTA");
                            strBuf.append("ATTTTTTTTTTTTTTTTTTTAATATAAAATTTTATATTAAATATATAAAT");
                            strBuf.append("TTATTTATATTAATAAATAATATATATATATATATAAATTTATTTATATT");
                            strBuf.append("AATAAATATTTTATAAAATAGATTAATATAAATAATTTATATATATATAT");
                            strBuf.append("ATATTTATTAATTAATAAATAAATAAATAATATTTATTTATAAATTAAAT");
                            strBuf.append("ATATTAATATATAATATATATATATATAAAATTTTATATTTATATATATA");
                            strBuf.append("ATATATATTTAATTAATTATGCTTATAATTAATTAATAATTTATATAAAT");
                            strBuf.append("CTATTTTATATTAATATAAATTTATTATATATTAAGTTGTTTATTTTTTT");
                            strBuf.append("TCTTCATTTAGGACCCATAATTATATAAATGATTTTCTATTGTTATTATA");
                            strBuf.append("AGAACTATAATTATGTTTTCATTGAGATATATTTATATATTTAAATAAAT");
                            strBuf.append("ATTTTATTATTTATATATATAAATATATTATTAAATTATTTATATTAATA");
                            strBuf.append("TAAATTATAATTTAAATATTTAATTATAATATAAAAATTTATATTATAAT");
                            strBuf.append("TTATATAAATATTTAATTATAATATAAAAATTTATATTATAATTTATATA");
                            strBuf.append("AATATTTAATTATAATATAAAAATTTATATTATAATTTATATAAATATTT");
                            strBuf.append("AATTATAATATAAAAATTTATATTATAATTTATATTTTAATTAAATATTA");
                            strBuf.append("ATTTATTTAAAAATTATAATTTAGAACCATTCATTTTTTTTTTACATATA");
                            strBuf.append("ATTATAAAATGAATTGCCTGACGAAAAGGGTTACCTTGATAGGGTAAATC");
                            strBuf.append("ATAAAGTTTATACTTTATTCATTAAATTATATTTAATAGAATTAAACTAT");
                            strBuf.append("TTCCAAAAGCTTCAAAAACTTTTGTGCATCGTACACTAAAATATAGATAA");
                            strBuf.append("TATATATATATTTATGTATTTATATAAAAATAACTCTTATATATAGATAA");
                            ArrayList<StringBuilder> sequences = new ArrayList<>();
                            sequences.add(strBuf);
                            Replicon re = new Replicon(Statistics.Type.CHROMOSOME, "CR1", 2, 1, sequences);
                            assertEquals("CR1", re.getName());
                            or.addReplicon(re);
                        }
                        or.stop();
                        or.finish();
                    }
                    su.stop();
                }
                gr.stop();
            }
            ki.stop();
        }
        db.stop();
    }

}