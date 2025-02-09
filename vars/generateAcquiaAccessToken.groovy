// vars/generateAccessToken.groovy

def call() {
    import groovy.json.JsonSlurper

    // Use Jenkins credentials binding to access the stored credentials
    withCredentials([usernamePassword(credentialsId: 'acquia-cloud-auth', 
                                      usernameVariable: 'CLIENT_ID', 
                                      passwordVariable: 'CLIENT_SECRET')]) {
        def tokenUrl = 'https://accounts.acquia.com/api/auth/oauth/token'
        def connection = new URL(tokenUrl).openConnection()
        connection.setRequestMethod('POST')
        connection.setDoOutput(true)
        connection.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')

        def postData = "grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}"
        connection.outputStream.withWriter { writer ->
            writer << postData
        }

        def response = connection.inputStream.text
        def jsonResponse = new JsonSlurper().parseText(response)

        return jsonResponse.access_token
    }
}
