
import { useState, useEffect } from "react";

const API = "http://localhost:8080";

function App() {

    // VARIÁVEIS DO REACT

    const [eventos, setEventos] = useState([]);
    const [nome, setNome] = useState("");
    const [local, setLocal] = useState("");
    const [capacidade, setCapacidade] = useState("");
    const [preco, setPreco] = useState("");
    const [editandoId, setEditandoId] = useState(null);


    // ATUALIZAR OS CAMPOS QUANDO O USUÁRIO DIGITAR

    function alterarNome(e) {
        setNome(e.target.value);
    }

    function alterarLocal(e) {
        setLocal(e.target.value);
    }

    function alterarCapacidade(e) {
        setCapacidade(e.target.value);
    }

    function alterarPreco(e) {
        setPreco(e.target.value);
    }


    // BUSCAR EVENTOS NO SPRING BOOT

    async function carregarEventos() {
        try {
            const resposta = await fetch(API + "/eventos");

            if (!resposta.ok) {
                throw new Error("Erro ao buscar eventos");
            }

            const dados = await resposta.json();

            setEventos(dados);

        } catch (erro) {
            alert(erro.message);
        }
    }


    // BUSCAR EVENTOS QUANDO O SITE ABRIR

    useEffect(function () {

        fetch(API + "/eventos")
            .then(function (resposta) {
                return resposta.json();
            })
            .then(function (dados) {
                setEventos(dados);
            })
            .catch(function (erro) {
                console.error(erro);
            });

    }, []);


    // LIMPAR O FORMULÁRIO

    function limparFormulario() {
        setNome("");
        setLocal("");
        setCapacidade("");
        setPreco("");
        setEditandoId(null);
    }


    // PREENCHER O FORMULÁRIO PARA EDITAR

    function editarEvento(evento) {
        setEditandoId(evento.id);
        setNome(evento.nome);
        setLocal(evento.local);

        window.scrollTo(0, 0);
    }


    // CADASTRAR OU ATUALIZAR EVENTO

    async function salvarEvento(e) {
        e.preventDefault();

        if (nome.trim() === "" || local.trim() === "") {
            alert("Preencha nome e local!");
            return;
        }

        let endereco;
        let metodo;
        let dados;

        if (editandoId === null) {

            // CADASTRAR

            if (
                capacidade === "" ||
                preco === "" ||
                !Number.isInteger(Number(capacidade)) ||
                Number(capacidade) <= 0 ||
                !Number.isFinite(Number(preco)) ||
                Number(preco) < 0
            ) {
                alert("Capacidade ou preço inválido!");
                return;
            }

            endereco = API + "/eventos";
            metodo = "POST";

            dados = {
                nome: nome,
                local: local,
                capacidade: Number(capacidade),
                precoIngresso: Number(preco)
            };

        } else {

            // EDITAR

            endereco = API + "/eventos/" + editandoId;
            metodo = "PUT";

            dados = {
                nome: nome,
                local: local
            };
        }

        try {
            const resposta = await fetch(endereco, {
                method: metodo,
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(dados)
            });

            if (!resposta.ok) {
                throw new Error("Erro ao salvar evento");
            }

            alert("Evento salvo com sucesso!");

            limparFormulario();

            await carregarEventos();

        } catch (erro) {
            alert(erro.message);
        }
    }


    // EXCLUIR EVENTO

    async function excluirEvento(id) {

        const confirmou = window.confirm(
            "Deseja excluir este evento?"
        );

        if (confirmou === false) {
            return;
        }

        try {
            const resposta = await fetch(
                API + "/eventos/" + id,
                {
                    method: "DELETE"
                }
            );

            if (!resposta.ok) {
                throw new Error(
                    "Erro ao excluir. Verifique se há ingressos."
                );
            }

            alert("Evento excluído!");

            if (editandoId === id) {
                limparFormulario();
            }

            await carregarEventos();

        } catch (erro) {
            alert(erro.message);
        }
    }


    // DEFINIR O TEXTO DOS BOTÕES

    let titulo = "Cadastrar evento";
    let textoBotao = "Cadastrar";

    if (editandoId !== null) {
        titulo = "Editar evento";
        textoBotao = "Salvar alterações";
    }


    // MOSTRAR A INTERFACE

    return (
        <div style={{
            maxWidth: 850,
            margin: "40px auto",
            padding: 25,
            background: "#f5f5f5",
            borderRadius: 12
        }}>

            <h1>Plataforma de Eventos</h1>

            <h2>{titulo}</h2>

            <form onSubmit={salvarEvento}>

                <input
                    placeholder="Nome do evento"
                    value={nome}
                    onChange={alterarNome}
                    required
                />

                <input
                    placeholder="Local"
                    value={local}
                    onChange={alterarLocal}
                    required
                />

                {editandoId === null && (
                    <>
                        <input
                            type="number"
                            min="1"
                            step="1"
                            placeholder="Capacidade"
                            value={capacidade}
                            onChange={alterarCapacidade}
                            required
                        />

                        <input
                            type="number"
                            min="0"
                            step="0.01"
                            placeholder="Preço"
                            value={preco}
                            onChange={alterarPreco}
                            required
                        />
                    </>
                )}

                <button type="submit">
                    {textoBotao}
                </button>

                {editandoId !== null && (
                    <button
                        type="button"
                        onClick={limparFormulario}
                    >
                        Cancelar edição
                    </button>
                )}

            </form>

            <h2>Eventos cadastrados</h2>

            {eventos.map(function (evento) {

                function clicarEditar() {
                    editarEvento(evento);
                }

                function clicarExcluir() {
                    excluirEvento(evento.id);
                }

                return (
                    <div
                        key={evento.id}
                        style={{
                            background: "white",
                            padding: 18,
                            marginBottom: 12,
                            borderRadius: 8,
                            border: "1px solid #ddd"
                        }}
                    >

                        <h3>{evento.nome}</h3>

                        <p>Local: {evento.local}</p>

                        <p>Capacidade: {evento.capacidade}</p>

                        <p>Preço: R$ {evento.precoIngresso}</p>

                        <button onClick={clicarEditar}>
                            Editar
                        </button>

                        <button onClick={clicarExcluir}>
                            Excluir
                        </button>

                    </div>
                );
            })}

        </div>
    );
}

export default App;
