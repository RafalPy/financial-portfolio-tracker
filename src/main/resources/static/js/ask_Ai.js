const chatDiv = document.getElementById('chat');

        function appendMessage(sender, text) {
            const div = document.createElement('div');
            div.className = 'message ' + sender;
            div.textContent = text;
            chatDiv.appendChild(div);
            chatDiv.scrollTop = chatDiv.scrollHeight;
        }

        async function sendMessage() {
            const input = document.getElementById('message');
            const message = input.value.trim();
            if (!message) return;

            appendMessage('user', message);
            input.value = '';

            try {
                const response = await fetch('/api/ai/chat', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ message })
                });

                if (!response.ok) throw new Error('AI service error');
                const text = await response.text();
                appendMessage('ai', text);
            } catch (err) {
                appendMessage('ai', 'Error: ' + err.message);
            }
        }

        // Optional: allow pressing Enter to send
        document.getElementById('message').addEventListener('keydown', e => {
            if (e.key === 'Enter') sendMessage();
        });
        window.sendMessage = sendMessage;