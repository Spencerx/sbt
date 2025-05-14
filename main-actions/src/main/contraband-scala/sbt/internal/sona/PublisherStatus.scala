/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.sona
/** https://central.sonatype.org/publish/publish-portal-api/#uploading-a-deployment-bundle */
final class PublisherStatus private (
  val deploymentId: String,
  val deploymentName: String,
  val deploymentState: sbt.internal.sona.DeploymentState,
  val purls: Vector[String]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: PublisherStatus => (this.deploymentId == x.deploymentId) && (this.deploymentName == x.deploymentName) && (this.deploymentState == x.deploymentState) && (this.purls == x.purls)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (17 + "sbt.internal.sona.PublisherStatus".##) + deploymentId.##) + deploymentName.##) + deploymentState.##) + purls.##)
  }
  override def toString: String = {
    "PublisherStatus(" + deploymentId + ", " + deploymentName + ", " + deploymentState + ", " + purls + ")"
  }
  private[this] def copy(deploymentId: String = deploymentId, deploymentName: String = deploymentName, deploymentState: sbt.internal.sona.DeploymentState = deploymentState, purls: Vector[String] = purls): PublisherStatus = {
    new PublisherStatus(deploymentId, deploymentName, deploymentState, purls)
  }
  def withDeploymentId(deploymentId: String): PublisherStatus = {
    copy(deploymentId = deploymentId)
  }
  def withDeploymentName(deploymentName: String): PublisherStatus = {
    copy(deploymentName = deploymentName)
  }
  def withDeploymentState(deploymentState: sbt.internal.sona.DeploymentState): PublisherStatus = {
    copy(deploymentState = deploymentState)
  }
  def withPurls(purls: Vector[String]): PublisherStatus = {
    copy(purls = purls)
  }
}
object PublisherStatus {
  
  def apply(deploymentId: String, deploymentName: String, deploymentState: sbt.internal.sona.DeploymentState, purls: Vector[String]): PublisherStatus = new PublisherStatus(deploymentId, deploymentName, deploymentState, purls)
}
