import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class Projet1 {

	public static Image filtreMedian(Image img) {
		ByteImage bytImg = new ByteImage(img);
		ArrayList<Integer> tab = new ArrayList<Integer>();
		for (int x = 1; x < bytImg.getXDim() - 1; ++x) {
			for (int y = 1; y < bytImg.getYDim() - 1; ++y) {
				tab.add(bytImg.getPixelXYByte(x, y));
				tab.add(bytImg.getPixelXYByte(x + 1, y));
				tab.add(bytImg.getPixelXYByte(x + 1, y + 1));
				tab.add(bytImg.getPixelXYByte(x - 1, y - 1));
				tab.add(bytImg.getPixelXYByte(x - 1, y));
				tab.add(bytImg.getPixelXYByte(x - 1, y + 1));
				tab.add(bytImg.getPixelXYByte(x + 1, y - 1));
				tab.add(bytImg.getPixelXYByte(x, y + 1));
				Collections.sort(tab);
				int valeur = (tab.get(3) + tab.get(4)) / 2;
				bytImg.setPixelXYByte(x, y, valeur);
				//System.out.println(valeur);
				tab.clear();
			}
		}
		return bytImg;
	}


	public static double[] histo(Image img, int numCanal) {

		double[] H = new double[256];
		Arrays.fill(H, 0.0);

		for (int x = 0; x < 255; x++) {
			for (int y = 0; y < 255; y++) {
				int nbPix = img.getPixelXYBByte(x,y,numCanal);
				H[nbPix] += 1.0;
			}
		}
		
		
		return H;
	}
	
	public static double[] histoWithoutBackground(Image img, Image mask, int numCanal) {

		double[] H = new double[256];
		Arrays.fill(H, 0.0);

		for (int x = 0; x < 255; x++) {
			for (int y = 0; y < 255; y++) {
				int nbPix = img.getPixelXYBByte(x,y,numCanal);
				int m= mask.getPixelXYBByte(x,y,0);
				if(m==255)
					H[nbPix] += 1.0;
			}
		}
		
		
		return H;
	}
	
	public static Image binarisation(Image img,int seuil) {
		
		int largeur = img.getXDim();
		int hauteur = img.getYDim();
		
		ByteImage res = new ByteImage(largeur, hauteur, 1, 1, 1);
		
		for (int x = 0; x < largeur; x++) {
			for (int y = 0; y < hauteur; y++) {
				int r = img.getPixelXYBByte(x, y, 0);
				int g = img.getPixelXYBByte(x, y, 1);
				int b = img.getPixelXYBByte(x, y, 2);
				if ( ((r+g+b)/3) < seuil ) {
				res.setPixelXYBByte(x, y, 0,255);
				} else {
					res.setPixelXYBByte(x, y, 0, 0);
				}
			}
		}
		
		return res;
			
	}
	
	
	
	public static double[] tabDiscret(double tab[]) {
		double[] discretisation = new double[tab.length/2];
		for (int i = 0, j=0; i < discretisation.length; i++, j+=2) {
			discretisation[i] = tab[j] + tab[j+1]/2;			
		}
		
	
		return discretisation;
		
	}
	
	public static double[] normalisationHisto(double tabHisto[], int pixel) {
		double[] tabNormaliser = new double[tabHisto.length];
		for (int i = 0; i < tabNormaliser.length; ++i) {
            tabNormaliser[i] = tabHisto[i]/pixel;
        }
        return tabNormaliser;
		
	}
	
	public static void rechercheImgSimilaires() {
		//List<String> results = new ArrayList<String>();
		 
		
	}
	
	public static double distanceSimiImg(double[] H1, double[] H2) {
        // A déclarer dans le parametre dans le main


        double distanceImg = 0;
        for (int i = 0; i < H1.length; ++i) {
            distanceImg += Math.sqrt(Math.pow(H1[i] - H2[i], 2));
        }

        return distanceImg;
    }
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Image test = ImageLoader.exec("/Users/amudhan/Documents/DUT 2A/TI/motos/000.jpg");
		Image test2 = ImageLoader.exec("/Users/amudhan/Documents/DUT 2A/TI/motos/001.jpg");

		//Image imageFiltree = filtreMedian(test);

		//niveauxDeGris.setColor(true);
		//Viewer2D.exec(imageFiltree);
		
		Image mask = binarisation(test, 240);
		Viewer2D.exec(mask);
		
		int pixelImg = test.xdim * test.ydim;
		int pixelImg2 = test2.xdim * test2.ydim;
		
		/*
		 * double[] tabR = histoWithoutBackground(test,mask, 0); double[] tabG =
		 * histoWithoutBackground(test,mask, 1); double[] tabB =
		 * histoWithoutBackground(test,mask, 2);
		 */
		
		double[] tabR = normalisationHisto(tabDiscret(histoWithoutBackground(test,mask, 0)), pixelImg);
		double[] tabG = normalisationHisto(tabDiscret(histoWithoutBackground(test,mask, 1)), pixelImg);
		double[] tabB = normalisationHisto(tabDiscret(histoWithoutBackground(test,mask, 2)), pixelImg);
		
		double[] tabR2 = normalisationHisto(tabDiscret(histoWithoutBackground(test2,mask, 0)), pixelImg2);
		double[] tabG2 = normalisationHisto(tabDiscret(histoWithoutBackground(test2,mask, 1)), pixelImg2);
		double[] tabB2 = normalisationHisto(tabDiscret(histoWithoutBackground(test2,mask, 2)), pixelImg2);
		/*
		 * try { HistogramTools.plotHistogram(tabR); HistogramTools.plotHistogram(tabG);
		 * HistogramTools.plotHistogram(tabB); } catch (Exception e) {
		 * 
		 * }
		 */
		
		TreeMap<Double, String> map = new TreeMap<>();
		
		File[] files = new File("/Users/amudhan/Documents/DUT 2A/TI/motos").listFiles();
		
		for(int i = 1; i<files.length; i++) {
			Image img = ImageLoader.exec(files[i].getAbsolutePath());
			img.setName(files[i].getAbsolutePath());
			if(img.getName() != test.getName()) {
				filtreMedian(img);
				Image maskImg = binarisation(img, 240);
	            double[] r2= normalisationHisto(tabDiscret(histoWithoutBackground(img, maskImg,0 )),pixelImg);
	            double[] g2=  normalisationHisto(tabDiscret(histoWithoutBackground(img, maskImg, 1)),pixelImg);
	            double[] b2= normalisationHisto(tabDiscret(histoWithoutBackground(img, maskImg, 2)),pixelImg);
	        
			
	    		double distanceImg = distanceSimiImg(tabR, tabR2)+ distanceSimiImg(tabG, tabG2) + distanceSimiImg(tabB, tabB2);
	    		
	    		map.put (distanceImg, img.getName());
	    		
			}
			else {
				System.out.println("error"); // exception à faire
			}
			
			//Viewer2D.exec(img);
		}
		
		int k = 0;
		for(Entry<Double , String> e:map.entrySet()) {
			System.out.println(e.getValue() + "=> " + e.getKey());
			Viewer2D.exec(ImageLoader.exec(e.getValue()));
			
			k++;
			
			if(k>10) break;
			
			
		}
		
		System.out.println(map);
				

	}
	
}

