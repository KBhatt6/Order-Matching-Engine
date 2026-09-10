    public class Order
    {

        private String id;
        private int shareNums;
        private int price;

        public Order(String id, int shares, int price)
        {
            this.id = id;
            shareNums = shares;
            this.price = price;
        }

        public String getId()
        {
            return id;
        }

        public int getShares()
        {
            return shareNums;
        }

        public void reduceShares(int x)
        {
             shareNums= shareNums-x;
        }

        public int getPrice()
        {
            return price;
        }
    }