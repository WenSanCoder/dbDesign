param(
    [Parameter(Mandatory = $true)]
    [string]$ServerHost,

    [string]$ServerUser = "root",

    [int]$LocalDashboardPort = 15672,

    [int]$LocalAmqpPort = 5672
)

Write-Host "Starting RabbitMQ SSH tunnel..."
Write-Host "Dashboard: http://127.0.0.1:$LocalDashboardPort"
Write-Host "AMQP:      127.0.0.1:$LocalAmqpPort"
Write-Host "Keep this window open while running the local Spring Boot demo."

ssh -N `
    -L "${LocalDashboardPort}:127.0.0.1:15672" `
    -L "${LocalAmqpPort}:127.0.0.1:5672" `
    "${ServerUser}@${ServerHost}"
