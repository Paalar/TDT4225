public enum BlockSize {

    BLOCK_SIZE(8192),
    N_BLOCKS(131072);

    private int block;

    BlockSize(int block) {
        this.block = block;
    }

    public int getBlock() {
        return block;
    }
}
