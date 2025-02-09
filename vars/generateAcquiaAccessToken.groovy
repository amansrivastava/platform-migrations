// vars/generateAccessToken.groovy
def call() {
    import groovy.json.JsonSlurper

    def clientId = credentials('oauth-client-id') // Use Jenkins credentials binding
    def clientSecret = credentials('oauth-client-secret')
    def connection = new URL("https://accounts.acquia.com/api/auth/oauth/token").openConnection()
    connection.setRequestMethod('POST')
    connection.setDoOutput(true)
    connection.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')

    def postData = "grant_type=client_credentials&client_id=${clientId}&client_secret=${clientSecret}"
    connection.outputStream.withWriter { writer ->
        writer << postData
    }

    def response = connection.inputStream.text
    def jsonResponse = new JsonSlurper().parseText(response)

    return jsonResponse.access_token
}
