import org.scalatest._

class MaxPairwiseProductTest extends FlatSpec with Matchers {

  "1,2,3" should "6" in {
    MaxPairwiseProduct.getMaxPairwiseProduct(List(1,2,3)) should be (6)
  }

  "4,4,1" should "4" in {
    MaxPairwiseProduct.getMaxPairwiseProduct(List(4,4,1)) should be (4)
  }

  "4,4,1,5" should "20" in {
    MaxPairwiseProduct.getMaxPairwiseProduct(List(4,4,1,5)) should be (20)
  }

  "68165 87637 74297 2904 32873 86010 87637 66131 82858 82935" should "6" in {
    MaxPairwiseProduct.getMaxPairwiseProduct(List(68165, 87637, 74297, 2904, 32873, 86010, 87637, 66131, 82858, 82935)) should be (BigInt(7537658370L))
  }

  "10, 100000" should "1000000" in {
    MaxPairwiseProduct.getMaxPairwiseProduct(List(10, 100000)) should be (BigInt(1000000))
  }

  "100000, 90000" should "9000000000" in {
    MaxPairwiseProduct.getMaxPairwiseProduct(List(100000, 90000)) should be (BigInt(9000000000L))
  }
}
