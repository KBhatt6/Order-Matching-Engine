import java.util.*;

    public class OrderMatcher {
        
        public static void main(String [] args)
        {
            System.out.println("=======================================================");
System.out.println("           LIMIT ORDER BOOK MATCHING ENGINE");
System.out.println("=======================================================");
System.out.println();
System.out.println("Simulates a single-instrument stock exchange order book.");
System.out.println("Buy and sell orders arrive one at a time from a random");
System.out.println("feed, and are matched using PRICE-TIME PRIORITY:");
System.out.println();
System.out.println("  - Highest bidding buyer matches against the");
System.out.println("    lowest asking seller first.");
System.out.println("  - Orders at the same price are matched in the");
System.out.println("    order they arrived (first come, first served).");
System.out.println("  - Orders that don't fully match rest in the book,");
System.out.println("    waiting for a future order to complete the trade.");
System.out.println();
System.out.println("Generating 40 random orders and running the match...");
System.out.println("-------------------------------------------------------");
System.out.println();

            int numberOfShares = 0;
            int priceTag = 0;
            int tag = 0;
            String id1 = "Buyer";
            String id2 = "Seller";
            
            int tradesExecuted = 0;
            int totalVolumeTraded = 0;
            
            ArrayList<Order> gateway = new ArrayList<Order>();



            for(int i = 0; i<40; i++)
            {
                int decider = (int) (Math.random() * 10)+ 1;
                if(decider %2 == 0)
                {
                    numberOfShares = (int) (Math.random()*5000);
                    priceTag = (int) (Math.random()*5000);
                    tag = (int) (Math.random()*20) +1;
                    Order BUYERS = new Order("Buyer" + String.valueOf(tag), numberOfShares, priceTag);
                    gateway.add(BUYERS);
                }

                else
                {
                    numberOfShares = (int) (Math.random()*5000);
                    priceTag = (int) (Math.random()*5000);
                    tag = (int) (Math.random()*20) +1;
                    Order SELLERS = new Order("Seller" + String.valueOf(tag), numberOfShares, priceTag);
                    gateway.add(SELLERS);   
                }


            }

            TreeMap<Integer, Deque<Order>> buyerGroup = new TreeMap<>();
            TreeMap<Integer, Deque<Order>> sellerGroup = new TreeMap<>();

            for(int i = 0; i<gateway.size(); i++)
            {
                Order current = gateway.get(i);
                    if(current.getId().charAt(0) == 'B')
                    {
                        while(current.getShares()>0 && sellerGroup.firstEntry() != null)
                        {
                            if(current.getPrice() < sellerGroup.firstKey())
                            {
                                break;
                            }
                            else
                            {
                                
                            int trade = Math.min(current.getShares(), sellerGroup.firstEntry().getValue().peekFirst().getShares());
                            tradesExecuted++;
                            totalVolumeTraded += trade;
                            System.out.println("TRADE #" + tradesExecuted + ": " + current.getId() + " x " + sellerGroup.firstEntry().getValue().peekFirst().getId() + " -- " + trade + " shares @ $" + sellerGroup.firstEntry().getKey());
                            current.reduceShares(trade);
                            sellerGroup.firstEntry().getValue().peekFirst().reduceShares(trade);
                            if(sellerGroup.firstEntry().getValue().peekFirst().getShares() == 0)
                            {
                                sellerGroup.firstEntry().getValue().pollFirst();
                                if(sellerGroup.firstEntry().getValue().isEmpty())
                                {
                                    sellerGroup.remove(sellerGroup.firstKey());
                                }
                            }
                                
                                
                            }
                        }
                        if (current.getShares() > 0) 
                        {
                            buyerGroup.computeIfAbsent(current.getPrice(), k -> new ArrayDeque<>()).addLast(current);
                        }
                        
                    }
                    else if(current.getId().charAt(0) == 'S')
                    {

                        while(current.getShares()>0 && buyerGroup.lastEntry() != null)
                        {
                            if(current.getPrice() >buyerGroup.lastKey())
                            {
                                break;
                            }
                            else
                            {
                            
                            int trade = Math.min(current.getShares(), buyerGroup.lastEntry().getValue().peekFirst().getShares());
                            tradesExecuted++;
                            totalVolumeTraded += trade;
                            System.out.println("TRADE #" + tradesExecuted + ": " + buyerGroup.lastEntry().getValue().peekFirst().getId() + " x " + current.getId() + " -- " + trade + " shares @ $" + buyerGroup.lastEntry().getKey());
                            current.reduceShares(trade);
                            buyerGroup.lastEntry().getValue().peekFirst().reduceShares(trade);  
                            if(buyerGroup.lastEntry().getValue().peekFirst().getShares() == 0)
                            {
                                buyerGroup.lastEntry().getValue().pollFirst();
                                if(buyerGroup.lastEntry().getValue().isEmpty())
                                {
                                    buyerGroup.remove(buyerGroup.lastKey());
                                }
                            }                            
                            }
                        }
                        if (current.getShares() > 0) 
                        {
                            sellerGroup.computeIfAbsent(current.getPrice(), k -> new ArrayDeque<>()).addLast(current);
                        }
                        
                    }
            }

            System.out.println();
            System.out.println("-------------------------------------------------------");
            System.out.println("SUMMARY");
            System.out.println("-------------------------------------------------------");
            System.out.println("Orders processed:      " + gateway.size());
            System.out.println("Trades executed:       " + tradesExecuted);
            System.out.println("Total shares traded:   " + totalVolumeTraded);
            System.out.println("=======================================================");

        }
        
    }