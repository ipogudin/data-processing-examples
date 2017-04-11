import scala.collection.immutable.HashSet

val s: String = "131600803df4895e\t1297100557\t2009\tAgriculture;History_of_the_world;China;Yangtze_River\tGrand_Canal_of_China\ttimeout"
val a = s.split(Array('\t', ' ', ';'))

object Filters {
  val stopWords = HashSet("timeout", "restart")
  def isStopWord(s: String): Boolean = stopWords.contains(s);
}

a.drop(3).filter(s => !Filters.isStopWord(s)).map(s => (s, 1))