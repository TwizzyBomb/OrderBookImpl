package com.company;

import java.util.*;

public class OrderBookImpl implements OrderBook {

    /* creating these two sorted sets because we want to be able to sort the offers by the highest price */
    private SortedSet<Map.Entry<Double, Integer>> sortedBuyOrdersSet = new TreeSet<Map.Entry<Double, Integer>>(
            new Comparator<Map.Entry<Double, Integer>>() {
                @Override
                public int compare(Map.Entry<Double, Integer> o1, Map.Entry<Double, Integer> o2) {
                    return o2.getKey().compareTo(o1.getKey());
                }
            });
    private SortedSet<Map.Entry<Double, Integer>> sortedSellOrdersSet = new TreeSet<Map.Entry<Double, Integer>>(
            new Comparator<Map.Entry<Double, Integer>>() {
                @Override
                public int compare(Map.Entry<Double, Integer> o1, Map.Entry<Double, Integer> o2) {
                    return o2.getKey().compareTo(o1.getKey());
                }
            });

    /* we're using these as copies to keep track of the data because we can't modify
        the SortedSets while operations are being conducted on them */
    private SortedMap<Double, Integer> sellOrdersMap = new TreeMap<Double, Integer>();
    private SortedMap<Double, Integer> buyOrdersMap = new TreeMap<Double, Integer>();

    @Override
    public void buy(double limitPrice, int quantity) {
        /* clear the sorted set */
        if( sortedBuyOrdersSet.size() > 0 )
            sortedBuyOrdersSet.clear();

        /* first we want to check whether there are any offers available */
        for(Map.Entry<Double, Integer> entry : sortedSellOrdersSet){
            if(limitPrice > entry.getKey() && quantity > 0){

                /* we want to decrease the quantity of the most expensive first */
                if( quantity >= entry.getValue() ){
                    /* subtract all */
                    quantity = quantity - entry.getValue();

                    /* set quantity to zero for removal later */
                    entry.setValue(0);

                }
                else{
                    /* subtract the */
                    entry.setValue(entry.getValue() - quantity);
                    quantity = 0;
                }
            }
        }

        /* remove map entries that have been depleted */
        for(Map.Entry<Double, Integer> entry : sortedSellOrdersSet){

            if(entry.getValue() == 0)
                /* want to remove it */
                sellOrdersMap.remove(entry.getKey());
        }
        /* clear and put back into sorted sell set */
        sortedSellOrdersSet.clear();
        sortedSellOrdersSet.addAll(sellOrdersMap.entrySet());

        /* if any quantity left we want to check if in map yet,
        and add to the quantity if so */
        if(quantity > 0)
            if(this.buyOrdersMap.containsKey(limitPrice)){
                int newQuantity = this.buyOrdersMap.get(limitPrice) + quantity;
                this.buyOrdersMap.put(limitPrice, newQuantity);
            }
            else
                /* just add to the map */
                this.buyOrdersMap.put(limitPrice, quantity);

        /* put back into sorted set for later */
        sortedBuyOrdersSet.addAll(buyOrdersMap.entrySet());
    }

    @Override
    public void sell(double limitPrice, int quantity) {

        /* clear the sorted set */
        if( sortedSellOrdersSet.size() > 0 )
            sortedSellOrdersSet.clear();

        /* first we want to check if the offer price is lower
        than the current buy offers, and sell for the highest
        offer if so, needs to be sorted so we compare against
        highest offer first - otherwise */
        for(Map.Entry<Double, Integer> entry : sortedBuyOrdersSet){
            if(limitPrice < entry.getKey() && quantity > 0){

                /* we want to decrease the quantity of the most expensive first */
                if( quantity >= entry.getValue() ){
                    /* subtract all */
                    quantity = quantity - entry.getValue();

                    /* remove entry */
                    sortedBuyOrdersSet.remove(entry);
                }
                else{
                    /* subtract the */
                    entry.setValue(entry.getValue() - quantity);
                    quantity = 0;
                }
            }
        }

        /* we want to check if there, and add to the quantity at that key */
        if(quantity > 0)
            if(this.sellOrdersMap.containsKey(limitPrice)){
                int newQuantity = this.sellOrdersMap.get(limitPrice) + quantity;
                this.sellOrdersMap.put(limitPrice, newQuantity);

            }
            else
                this.sellOrdersMap.put(limitPrice, quantity);

        /* put back into sorted set for later */
        sortedSellOrdersSet.addAll(sellOrdersMap.entrySet());

    }

    @Override
    public String getBookAsString() {
        // TODO implement
        StringBuilder sb = new StringBuilder();
        sb.append("{bids={");
        int counter = 0;

        for(Map.Entry<Double, Integer> entry : sortedBuyOrdersSet){

            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            if(!(counter==this.buyOrdersMap.size()-1))
                sb.append(", ");
            else
                sb.append("}");

            counter++;
        }

        counter = 0;
        sb.append(", offers={");
        for(Map.Entry<Double, Integer> entry : sellOrdersMap.entrySet()){
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());

            /* add commas unless it's the last record */
            if( !(counter==this.sellOrdersMap.size()-1) )
                sb.append(", ");

            counter++;
        }
        sb.append("}}");

        return sb.toString();
    }

}
